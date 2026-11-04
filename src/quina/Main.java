package quina;
import quinas.lexer.Lexer;
import quinas.node.Start;
import quinas.parser.*;

import java.io.FileReader;
import java.io.PushbackReader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String file = "expressao.quinas";
			
			Lexer lex = new Lexer(
					new PushbackReader(new FileReader(file), 1024));
			
			Parser p = new Parser(lex);
			
			Start tree = p.parse();
			tree.apply(new ASTDisplay());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
