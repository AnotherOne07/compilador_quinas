package quina;

import quinas.analysis.DepthFirstAdapter;
import quinas.node.Node;
import quinas.node.Token;

public class ASTDisplay extends DepthFirstAdapter {
    private int indent = 0;

    @Override
    public void defaultIn(Node node) {
        imprimirIndentacao();
        System.out.println(node.getClass().getSimpleName().replaceAll("^A", ""));
        indent++;
    }

    @Override
    public void defaultOut(Node node) {
        indent--;
    }

    @Override
    public void defaultCase(Node node) {
        if (node instanceof Token) {
            imprimirIndentacao();
            Token token = (Token) node;
            System.out.println(token.getClass().getSimpleName() + " [" + token.getText().trim() + "]");
        }
    }

    private void imprimirIndentacao() {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
    }
}