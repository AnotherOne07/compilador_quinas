package quina;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import quinas.analysis.DepthFirstAdapter;
import quinas.node.EOF;
import quinas.node.Node;
import quinas.node.Start;
import quinas.node.Token;

public class ASTDisplay extends DepthFirstAdapter {

    @SuppressWarnings("rawtypes")
    private Stack parents = new Stack();

    public ASTDisplay() {
    }

    @Override
    public void outStart(Start node) {
        JFrame frame = new JFrame("AST Displayer - Quinas");
        JTree tree = new JTree((DefaultMutableTreeNode) parents.pop());
        JScrollPane pane = new JScrollPane(tree);

        expandAll(tree);

        /* Window listener para fechar a aplicação ao fechar a janela */
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        frame.setSize(400, 600);
        frame.getContentPane().add(pane);
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela
        frame.setVisible(true);
    }

    /*
     * Conforme encontramos nós não-terminais, adicionamos na pilha
     */
    @Override
    public void defaultIn(Node node) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode(
                node.getClass().getName().substring(node.getClass().getName().lastIndexOf('.') + 1)
        );
        parents.push(thisNode);
    }

    /*
     * Ao sair de um não-terminal, o pai é o elemento anterior da pilha.
     * Desempilhamos o nó atual e o adicionamos ao nó pai.
     */
    @Override
    public void defaultOut(Node node) {
        DefaultMutableTreeNode thisNode = (DefaultMutableTreeNode) parents.pop();
        ((DefaultMutableTreeNode) parents.peek()).add(thisNode);
    }

    /*
     * Terminais - o pai está no topo da pilha, então apenas adicionamos a ele
     */
    @Override
    public void defaultCase(Node node) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode(((Token) node).getText());
        ((DefaultMutableTreeNode) parents.peek()).add(thisNode);
    }

    @Override
    public void caseEOF(EOF node) {
        // Ignora o token EOF para não sujar a árvore gráfica
    }

    /*
     * Expande todos os nós da árvore automaticamente
     */
    public static void expandAll(JTree tree) {
        Object root = tree.getModel().getRoot();
        if (root != null) {
            expandAll(tree, new TreePath(root));
        }
    }

    public static void expandAll(JTree tree, TreePath path) {
        for (Iterator<?> i = extremalPaths(tree.getModel(), path, new HashSet<>()).iterator(); i.hasNext(); ) {
            tree.expandPath((TreePath) i.next());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Collection extremalPaths(TreeModel data, TreePath path, Collection result) {
        result.clear();
        if (data.isLeaf(path.getLastPathComponent())) {
            return result;
        }
        extremalPathsImpl(data, path, result);
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void extremalPathsImpl(TreeModel data, TreePath path, Collection result) {
        Object node = path.getLastPathComponent();
        boolean hasNonLeafChildren = false;
        int count = data.getChildCount(node);

        for (int i = 0; i < count; i++) {
            if (!data.isLeaf(data.getChild(node, i))) {
                hasNonLeafChildren = true;
            }
        }

        if (!hasNonLeafChildren) {
            result.add(path);
        } else {
            for (int i = 0; i < count; i++) {
                Object child = data.getChild(node, i);
                if (!data.isLeaf(child)) {
                    extremalPathsImpl(data, path.pathByAddingChild(child), result);
                }
            }
        }
    }
}