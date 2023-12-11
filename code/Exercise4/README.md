# Code of your exercise

## The code is available in the 'java-parser'

package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.*;
import java.util.*;

public class NoGetter extends VoidVisitorWithDefaults<Void> {
    private final Set<VariableDeclarator> attributes;
    private final Set<MethodDeclaration> getters;
    private final Map<String, Set<String>> reportedAttributesPerClass;

    public NoGetter() {
        attributes = new HashSet<>();
        getters = new HashSet<>();
        reportedAttributesPerClass = new HashMap<>();
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        try {
            FileOutputStream fos = new FileOutputStream("report.md");
            PrintWriter pw = new PrintWriter(fos);

            for (String className : reportedAttributesPerClass.keySet()) {
                pw.println("## " + className);
                pw.println("The following attributes are not accessed through a getter:");
                for (String attributeName : reportedAttributesPerClass.get(className)) {
                    pw.println("* " + attributeName);
                }
                pw.println();
            }

            pw.close();
            fos.close();
        } catch (Exception e) {
            System.err.println("Could not write report.md");
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        attributes.clear();
        getters.clear();

        for (FieldDeclaration field : declaration.getFields())
            visit(field, arg);
        for (MethodDeclaration method : declaration.getMethods())
            visit(method, arg);

        Set<String> reportedAttributes = new HashSet<>();
        for (VariableDeclarator attr : attributes) {
            if (!hasGetter(attr))
                reportedAttributes.add(attr.getNameAsString());
        }

        reportedAttributesPerClass.put(declaration.getNameAsString(), reportedAttributes);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPublic()) return;

        attributes.addAll(declaration.getVariables());
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;
        String methodName = declaration.getNameAsString();
        if (!methodName.startsWith("get")) return;

        getters.add(declaration);
    }

    private boolean hasGetter(VariableDeclarator attr) {

        NodeList<Statement> statements;
        Statement statement;
        for (MethodDeclaration getter : getters) {
            if (isGetter(getter, attr))
                return true;
        }

        return false;
    }

    private boolean isGetter(MethodDeclaration method, VariableDeclarator attr) {
        if (!method.getNameAsString().equals(getGetterName(attr.getNameAsString())))
            return false;

        if (!method.getBody().isPresent())
            return false;

        BlockStmt body = method.getBody().get();

        if (body.getStatements().size() != 1)
            return false;

        Statement statement = body.getStatement(0);

        if (!(statement.isReturnStmt()))
            return false;

        ReturnStmt returnStmt = statement.asReturnStmt();

        if (!returnStmt.getExpression().isPresent())
            return false;

        Expression expression = returnStmt.getExpression().get();

        return expression.toString().equals(attr.getNameAsString()) ||
                expression.toString().equals("this." + attr.getNameAsString());
    }

    private String getGetterName(String attributeName) {
        return "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
    }
}
