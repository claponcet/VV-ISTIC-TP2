package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        System.out.println(declaration.getNameAsString());
        for (FieldDeclaration field : declaration.getFields())
            field.accept(this, arg);
        for (MethodDeclaration method : declaration.getMethods())
            visit(method, arg);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        System.out.println("\t" + declaration.toString());
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println("\t" + declaration.getDeclarationAsString(true, true));
    }

}
