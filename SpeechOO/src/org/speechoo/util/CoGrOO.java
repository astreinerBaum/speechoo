package org.speechoo.util;

import java.util.List;

import br.usp.pcs.lta.cogroo.configuration.LegacyRuntimeConfiguration;
import br.usp.pcs.lta.cogroo.configuration.RuntimeConfigurationI;
import br.usp.pcs.lta.cogroo.entity.Mistake;
import br.usp.pcs.lta.cogroo.grammarchecker.Cogroo;
import org.speechoo.SpeechOO;

/**
 *
 * @author 10080000501
 */
public class CoGrOO {
    public static int aux, gramaticalFlag, cellNumber, cellSeparator, length;
    public static Object dataNameGramaticals[][];
    public static String wrong;
public static void main(String entrada, int tokensLength) {

String pathToResources = System.getProperty("user.home")+"/target/cogroo";
RuntimeConfigurationI config = new LegacyRuntimeConfiguration(pathToResources);
// cria instância do corretor gramatical
TableGramatical c = new TableGramatical();
Cogroo theCogroo = new Cogroo(config);
// prepara para obter o texto
// aguarda texto

   // verifica erros do texto
   List<Mistake> erros = theCogroo.checkText(entrada);
   //int contador = 1;
    System.out.println(entrada);
   // imprime detalhes de cada erro
   for (Mistake erro : erros) {
       TableGramatical.cellSeparator = erro.getSuggestions().length;
       System.out.println("eu");
       TableGramatical.modelGramatical.setRowCount(TableGramatical.cellSeparator+1);
       TableGramatical.modelGramatical.setColumnCount(TableGramatical.cellSeparator);
       //System.out.println("Erro " + contador++ + " -");
      // System.out.println("   Regra          '" + erro.getRuleIdentifier() + "'");
       //System.out.println("   Mensagem curta '" + erro.getShortMessage() + "'");
      // System.out.println("   Mensagem longa '" + erro.getFullMessage() + "'");
      // System.out.println("   Texto com erro '" + entrada.substring(erro.getStart(), erro.getEnd()) + "'");
      // System.out.print(  "   Sugestões " );
      // System.out.println("vai2");
     System.out.println(erro.getSuggestions().length);
     TableGramatical.TableGramatical.setValueAt(0, 0, 0);
     TableGramatical.TableGramatical.setValueAt(entrada.substring(erro.getStart(), erro.getEnd()), 0, 1);
     c.Gramatical();
     for(aux=0; aux<erro.getSuggestions().length; aux++){
     TableGramatical.TableGramatical.setValueAt(aux+1, aux+1, 0);
     }
     for(aux=0; aux<erro.getSuggestions().length; aux++){
     TableGramatical.TableGramatical.setValueAt(erro.getSuggestions()[aux], aux+1, 1);
     }
     wrong = entrada.substring(erro.getStart(), erro.getEnd());
     length = tokensLength;
     SpeechOO.dic.setEnabled(false);
     gramaticalFlag = 1;
     SpeechOO.gram.setEnabled(true);
     aux = CoGrOO.length-(CoGrOO.wrong.split(" ").length-1);
     System.out.println("palavra com erro:"+wrong.split(" ").length);
     System.out.println("Tamanho da Frase:"+CoGrOO.length);
     System.out.println("Parada do FOR:" +CoGrOO.aux);
     System.out.println(erro.getSuggestions().length);
   }
System.out.println(entrada);
 

}
}

