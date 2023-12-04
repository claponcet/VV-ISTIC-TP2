package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.*;
import java.util.*;

public class NoGetter extends VoidVisitorWithDefaults<Void> {
    private Set<String> attributeNames;
    private Set<String> getterNames;
    private Map<String, Set<String>> reportedAttributesPerClass;

    public NoGetter() {
        attributeNames = new HashSet<>();
        getterNames = new HashSet<>();
        reportedAttributesPerClass = new HashMap<>();
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        try {
            FileOutputStream fos = new FileOutputStream(new File("report.md"));
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
        }
        catch (Exception e) {
            System.err.println("Could not write report.md");
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        attributeNames.clear();
        getterNames.clear();

        for (FieldDeclaration field : declaration.getFields())
            visit(field, arg);
        for (MethodDeclaration method : declaration.getMethods())
            visit(method, arg);

        Set<String> reportedAttributes = new HashSet<>();
        for (String attributeName : attributeNames) {
            if (getterNames.contains(getGetterName(attributeName))) continue;
            reportedAttributes.add(attributeName);
        }

        reportedAttributesPerClass.put(declaration.getNameAsString(), reportedAttributes);

        for (String className : reportedAttributesPerClass.keySet()) {
            System.out.println("## " + className);
            System.out.println("The following attributes are not accessed through a getter:");
            for (String attributeName : reportedAttributesPerClass.get(className)) {
                System.out.println("* " + attributeName);
            }
            System.out.println();
        }
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPublic()) return;
        String attributeName = declaration.getVariable(0).getNameAsString();

        System.out.println(attributeName);

        attributeNames.add(attributeName);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic() || !declaration.getParameters().isEmpty()) return;
        String methodName = declaration.getNameAsString();
        if (!methodName.startsWith("get")) return;

        System.out.println(methodName);

        getterNames.add(methodName);
    }

    public String getGetterName(String attributeName) {
        return "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
    }
}
