/**
 * Este pacote provï¿½ acesso ï¿½ biblioteca TerraManager da famï¿½lia TerraLib, atravï¿½s da camada de conversï¿½o de tipos TerraJava (JNI).
 * @see <a href='bind-terrajava-jni.html'>TerraJava</a>
 * @author Claudio Henrique Bogossian
 * @file TerraJSP.java
 * Este ï¿½ o cï¿½digo fonte da classe TerraJSP.
 */
package br.org.funcate.terrajava.persistencia;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Vector;

/**
 * <pre>
 * Esta classe armazena chamadas a mï¿½todos nativos implementados na classe TerraJava (JNI), permitindo a implementaï¿½ï¿½o
 * de sistemas de informaï¿½ï¿½es geogrï¿½ficas na plataforma JAVA mantendo o estilo de programaï¿½ï¿½o do paradigma da orientaï¿½ï¿½o a objetos.
 * A classe TerraJSP fornece mï¿½todos para estabelecimento de uma conexï¿½o a um servidor de bancos de dados, exploraï¿½ï¿½o do
 * conteï¿½do do banco e um canvas (abstraï¿½ï¿½o de uma ï¿½rea para desenho) que pode ser utilizado para visualizar a
 * componente espacial dos objetos geogrï¿½ficos do banco de dados. O desenho sobre o canvas pode ser materializado atravï¿½s
 * de imagens no formato PNG, JPEG ou GIF.
 * 
 * Atravï¿½s desta interface de programaï¿½ï¿½o ï¿½ possï¿½vel realizar:
 * 
 * 1- Exploraï¿½ï¿½o do conteï¿½do geogrï¿½fico e alfanumerico armazenado em um banco de dados relacional modelo TerraLib;
 * 2- Operaï¿½ï¿½es de georreferenciamento de camadas de dados vetoriais cujo conteï¿½do representa a malha viï¿½ria de uma localidade;
 * 3- Criaï¿½ï¿½o de camadas de dados geogrï¿½ficos, denominados "Layers", atravï¿½s de processo de importaï¿½ï¿½o de arquivo ShapeFile;
 * 4- Exportaï¿½ï¿½o de camadas de dados geogrï¿½ficos vetoriais para arquivo em formato ShapeFile, formato de transporte de dados
 * geogrï¿½ficos desenvolvido pela empresa ESRI - Environmental Systems Research Institute;
 * 5- Recuperaï¿½ï¿½o de mapas baseados nas camadas de dados disponï¿½veis na base de dados TerraLib nos formatos de compressï¿½o
 * de imagens JPEG (Joint Photographic Experts Group), PNG (Portable Network Graphics format) e GIF (Graphics Interchange Format);
 * 6- Geraï¿½ï¿½o de mapas temï¿½ticos baseado em algoritmos de classificaï¿½ï¿½o baseado em um atributo especifico relacionado ï¿½s geometrias de uma
 * camada de dados vetorial.
 * 
 * Antes de detalhar os mï¿½todos disponï¿½veis na classe TerraJSP ï¿½ preciso falar de trï¿½s conceitos empregados no projeto da extensï¿½o:
 * vista corrente, temas ativos e representaï¿½ï¿½o corrente. A vista corrente define a projeï¿½ï¿½o em que todos os temas e objetos do banco
 * de dados serï¿½o visualizados. Ela define tambï¿½m os temas que estarï¿½o disponï¿½veis para visualizaï¿½ï¿½o e consulta. Qualquer mï¿½todo que
 * receba valores de coordenadas como parï¿½metro (drawPoint, drawText, drawBox entre outros) assumirï¿½ que as coordenadas encontram-se
 * na mesma projeï¿½ï¿½o da vista corrente. Para comeï¿½ar a desenhar um tema ou objeto de um tema, ï¿½ necessï¿½rio ter uma vista corrente ativada
 * (mï¿½todo setCurrentView).
 * 
 * Os temas ativos sï¿½o utilizados nas operaï¿½ï¿½es de localizaï¿½ï¿½o de objetos e desenho no canvas. Pode-se ter no mï¿½ximo dois temas ativos
 * por vez:
 * 
 * tema corrente: define o tema no qual certas operaï¿½ï¿½es deverï¿½o ser realizadas. Somente os temas correntes podem ser desenhados sobre
 * o canvas. Esta documentaï¿½ï¿½o explicita em cada mï¿½todo os prï¿½ requisitos para a sua utilizaï¿½ï¿½o. Ter um tema corrente definido ï¿½
 * prï¿½ requisito para vï¿½rios mï¿½todos.
 * 
 * tema de referï¿½ncia: geralmente utilizado como referï¿½ncia para localizaï¿½ï¿½o de objetos relacionados com o tema corrente.
 * Por exemplo, se no tema corrente temos objetos que representam queimadas (pontos) e no tema de referï¿½ncia temos objetos que
 * representam municï¿½pios (polï¿½gonos), poderï¿½amos realizar a seguinte consulta espacial: "localizar o municï¿½pio que contï¿½m um determinado
 * ponto de queimada".
 * 
 * O ï¿½ltimo conceito importante ï¿½ o de representaï¿½ï¿½o corrente. Para cada um dos tipos de tema (corrente e referï¿½ncia) ï¿½ possï¿½vel
 * indicar quais as respectivas representaï¿½ï¿½es geomï¿½tricas ativas, pontos, linhas e poligonos. Estas representaï¿½ï¿½es afetam as operaï¿½ï¿½es
 * espaciais e o desenho de objetos selecionados.
 * </pre>
 * 
 * @author Claudio Henrique Bogossian
 * @version 1.0, 04/03/2009
 */

public class TerraJava {

	/**
	 * Mï¿½todo nativo para conectar-se ao banco de dados modelo TerraLib.
	 * 
	 * @param host
	 *            Nome ou IP do servidor onde roda o serviï¿½o do gerenciador de
	 *            banco de dados.
	 * @param user
	 *            Nome do usuï¿½rio com permissï¿½o de acesso ao banco de dados. ï¿½
	 *            necessï¿½ria a permissï¿½o de leitura e escrita no banco, para o
	 *            uso de funï¿½ï¿½es de importaï¿½ï¿½o de dados e criaï¿½ï¿½o de "layers".
	 * @param password
	 *            Senha do usuï¿½rio para validaï¿½ï¿½o do acesso.
	 * @param database
	 *            Nome do banco de dados.
	 * @param port
	 *            Nï¿½mero da porta usada pelo gerenciador de banco de dados para
	 *            receber conexï¿½es.
	 * @param dbType
	 *            Tipo de gerenciador de banco de dados. Parï¿½metro usado para
	 *            selacionar o driver correto para a conexï¿½o.
	 * 
	 *            <pre>
	 * Os tipos suportados sï¿½o:
	 * 
	 * TeMySQLDB = 1,           Para conexï¿½o com MySQL
	 * TePostgreSQLDB = 2,      Para conexï¿½o com PostgreSQL que nï¿½o possua a extensï¿½o espacial PostGIS
	 * TePostGISDB = 3,         Para conexï¿½o com PostgreSQL que possua a extensï¿½o espacial PostGIS
	 * TeADODB = 4,             Para conexï¿½o com Microsoft Access (only in Windows).
	 * TeADOOracleDB = 5,       Para conexï¿½o com Oracle usando Microsoft ADO support (only in Windows).
	 * TeADOSqlServerDB = 6,    Para conexï¿½o com SQL Server (only in Windows).
	 * TeOracleSpatialDB = 7,   Para conexï¿½o com Oracle Spatial usando o driver OCI
	 * TeOracleOCIDB = 8,       Para conexï¿½o com Oracle sem suporte espacial usando o driver OCI
	 * TeIBFirebirdDB = 9       Para conexï¿½o com Firebird
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) caso tenha sucesso na conexï¿½o e Falso (false)
	 *         caso contrï¿½rio.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou nï¿½o seja possï¿½vel conectar ao banco.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Nï¿½o pï¿½de encontrar o host, ou a autenticaï¿½ï¿½o fornecida por usuï¿½rio e senha ï¿½ invï¿½lida,
	 * o banco especificado pelo parï¿½metro database nï¿½o existe ou nï¿½o pode ser acessado,
	 * ou ainda o modelo de dados TerraLib ï¿½ antigo, diferente de 3.3.1, e portanto nï¿½o suportado.
	 * 
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * String sessionId = "123456";
	 * int port = 5432;
	 * int dbType = 2;
	 * private TerraJSP terraJSP;
	 * terraJSP = new TerraJSP();
	 * try{
	 *     terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *     String mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *     mensagemErro += terraJSP.errorMessage(sessionId);
	 *     throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * A existï¿½ncia de um banco de dados geogrï¿½fico modelo TerraLib.
	 * </pre>
	 */

	public native void connect(String host, String user, String password,
			String database, int port, int dbType, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	public native boolean generateTerralibConceptualModel(String sessionId)
			throws IllegalAccessException, InstantiationException;

	public native boolean isConnected(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Mï¿½todo nativo para configurar como corrente uma determinada vista
	 * existente no banco de dados para o usuï¿½rio usado para conectar. A
	 * projeï¿½ï¿½o definida para a vista serï¿½ adotada como padrï¿½o para todas as
	 * operaï¿½ï¿½es de desenho de objetos geogrï¿½ficos sobre o canvas.
	 * 
	 * @param view
	 *            Nome de uma vista existente no banco de dados e que tenha sido
	 *            criada pelo usuï¿½rio fornecido pelo parï¿½metro userName. As
	 *            vistas sï¿½o de propriedade do usuï¿½rio que as criou.
	 * @param userName
	 *            nome do usuï¿½rio dono da vista selecionada para as operaï¿½ï¿½es
	 *            que exigem uma vista corrente como prï¿½ requisito.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true), se foi bem sucedido na operaï¿½ï¿½o de seleï¿½ï¿½o da
	 *         vista como vista corrente ou Falso (false) caso contrï¿½rio.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou nï¿½o seja possï¿½vel configurar como corrente a
	 *             vista fornecida.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * A vista especificada nï¿½o existe ou nï¿½o ï¿½ de propriedade do usuï¿½rio usado na solicitaï¿½ï¿½o, parï¿½metro userName,
	 * ou ainda o parï¿½metro view foi passado como uma String vazia.
	 * 
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * int port = 5432;
	 * int dbType = 2;
	 * 
	 * String sessionId = "123456";
	 * String view = "web";
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao configurar a vista "+view+" como corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Conhecer o nome de uma vista vï¿½lida.
	 * Conhecer o nome do usuï¿½rio dono da vista escolhida.
	 * </pre>
	 */
	public native boolean setCurrentView(String view, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo nativo para acessar o nome da vista corrente.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return O nome da vista corrente.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou nï¿½o seja possï¿½vel recuperar a vista corrente.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * Nï¿½o existe uma vista definida como corrente.
	 * 
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * int port = 5432;
	 * int dbType = 2;
	 * 
	 * String sessionId = "123456";
	 * String view = "web";
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    // definindo a vista corrente
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    
	 *    String currentView = terraJSP.getCurrentView(sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * </pre>
	 * 
	 */
	public native String getCurrentView(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Mï¿½todo nativo para definir uma tema como corrente ou referï¿½ncia. O tema
	 * corrente pode ser desenhado no canvas, jï¿½ o tema de referï¿½ncia ï¿½ usado
	 * para operaï¿½ï¿½es de restriï¿½ï¿½o ou seleï¿½ï¿½o espacial e operaï¿½ï¿½es topolï¿½gicas.
	 * 
	 * @param theme
	 *            Nome de um tema vï¿½lido, existente na ï¿½rvore de temas da vista
	 *            corrente.
	 * @param themeType
	 *            Tipo de definiï¿½ï¿½o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de referï¿½ncia.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true), se foi bem sucedido na operaï¿½ï¿½o de seleï¿½ï¿½o do
	 *         tema como tema corrente ou de referï¿½ncia ou Falso (false) caso
	 *         contrï¿½rio.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou nï¿½o seja possï¿½vel definir o tema corrente ou de
	 *             referï¿½ncia.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * O parametro theme, ï¿½ um nome de tema que nï¿½o existe na ï¿½rvore de temas da vista corrente.
	 * O parï¿½metro theme ï¿½ uma string vazia.
	 * O tema encontrado nï¿½o possui uma representaï¿½ï¿½o vetorial ativa.
	 * Entende-se como representaï¿½ï¿½o vetorial, os tipos de dados vetoriais basicos definidos pela TerraLib: cï¿½lulas, poligonos, linhas, pontos e texto.
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * String sessionId = "123456";
	 * int port = 5432;
	 * int dbType = 2;
	 * 
	 * String view = "web";
	 * String theme = "Limite";
	 * int themeType = 0;
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    else
	 *       if(!terraJSP.setTheme(theme, themeType, sessionId))
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("referï¿½ncia"))+".";
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * </pre>
	 * 
	 */
	public native boolean setTheme(String theme, int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Mï¿½todo nativo para definir uma tema como corrente ou referï¿½ncia. O tema
	 * corrente pode ser desenhado no canvas, jï¿½ o tema de referï¿½ncia ï¿½ usado
	 * para operaï¿½ï¿½es de restriï¿½ï¿½o ou seleï¿½ï¿½o espacial e operaï¿½ï¿½es topolï¿½gicas.
	 * 
	 * @param theme
	 *            Nome de um tema vï¿½lido, existente na ï¿½rvore de temas da vista
	 *            corrente.
	 * @param themeType
	 *            Tipo de definiï¿½ï¿½o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de referï¿½ncia.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true), se foi bem sucedido na operaï¿½ï¿½o de seleï¿½ï¿½o do
	 *         tema como tema corrente ou de referï¿½ncia ou Falso (false) caso
	 *         contrï¿½rio.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou nï¿½o seja possï¿½vel definir o tema corrente ou de
	 *             referï¿½ncia.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * O parametro theme, ï¿½ um nome de tema que nï¿½o existe na ï¿½rvore de temas da vista corrente.
	 * O parï¿½metro theme ï¿½ uma string vazia.
	 * O tema encontrado nï¿½o possui uma representaï¿½ï¿½o vetorial ativa.
	 * Entende-se como representaï¿½ï¿½o vetorial, os tipos de dados vetoriais basicos definidos pela TerraLib: cï¿½lulas, poligonos, linhas, pontos e texto.
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * String sessionId = "123456";
	 * int port = 5432;
	 * int dbType = 2;
	 * 
	 * String view = "web";
	 * Vector<int> themeList = new Vector();
	 * themeList
	 * 
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    else
	 *       if(!terraJSP.setThemesPriorityOrder(themeList, sessionId))
	 *          mensagemErro = "Falhou ao redefinir a ordem dos temas.";
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao executar update na ordem dos temas: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * </pre>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public native boolean setThemesPriorityOrder(Vector themeList,
			boolean persist, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo nativo para acessar o nome do tema definido como corrente ou
	 * referï¿½ncia.
	 * 
	 * @param themeType
	 *            Tipo de definiï¿½ï¿½o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de referï¿½ncia.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Acesso ao nome do tema definido previamente como tema corrente ou
	 *         de referï¿½ncia.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou nï¿½o seja possï¿½vel acessar o tema corrente ou de
	 *             referï¿½ncia.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * Nï¿½o foi definida uma vista corrente.
	 * Nï¿½o existe um tema definido como corrente ou de referï¿½ncia.
	 * 
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * String sessionId = "123456";
	 * int port = 5432;
	 * int dbType = 2;
	 * 
	 * String view = "web";
	 * String theme = "Limite";
	 * int themeType = 0;
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    else
	 *       if(!terraJSP.setTheme(theme, themeType, sessionId))
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("referï¿½ncia"))+".";
	 *    // recuperando o tema corrente.
	 *    String currentTheme = terraJSP.getTheme(themeType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente ou de referï¿½ncia: {@link #setTheme(String, int, String)}
	 * </pre>
	 * 
	 */
	public native String getTheme(int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * ! Este mï¿½todo deve ser excluido. A assinatura nï¿½o necessita dos
	 * parï¿½metros Vector vectorObj e Double doubleObj
	 */
	@SuppressWarnings("unchecked")
	private native Vector getThemeBox(int themeType, String restriction,
			Vector vectorObj, Double doubleObj, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Mï¿½todo nativo que permite obter as coordenadas do retï¿½ngulo envolvente
	 * dos objetos do tema corrente ou de referï¿½ncia levando em consideraï¿½ï¿½o a
	 * restriï¿½ï¿½o se esta tiver sido definida.
	 * 
	 * @param themeType
	 *            Tipo de definiï¿½ï¿½o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de referï¿½ncia.
	 * </pre>
	 * @param restriction
	 *            Permite aplicar uma restriï¿½ï¿½o ao conjunto de objetos
	 *            geogrï¿½ficos apontados pelo tema corrente ou de referï¿½ncia. O
	 *            formato permitido para esta restriï¿½ï¿½o ï¿½ uma <b>clausula
	 *            <i>where</i></b> no formato SQL ANSI. A restriï¿½ï¿½o deve seguir
	 *            o formato
	 *            "nome_da_coluna + operador + valor ou lista_de_valores".
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Um vetor de quatro elementos float contendo as coordenadas do
	 *         retï¿½ngulo envolvente dos objetos do tema corrente ou de
	 *         referï¿½ncia (x1,x2,y1,y2 respectivamente). Os valores de
	 *         coordenadas encontram-se no sistema de projeï¿½ï¿½o indicado pela
	 *         vista corrente.
	 * 
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou a restriï¿½ï¿½o tenha gerado um erro na clausula
	 *             <i>where</i> de filtro.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * Nï¿½o foi definida uma vista corrente.
	 * Nï¿½o existe um tema definido como corrente ou de referï¿½ncia.
	 * O tema definido como corrente ou de referï¿½ncia nï¿½o possui representaï¿½ï¿½o vetorial ativa.
	 * @see <a href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * int port = 5432;
	 * int dbType = 2;
	 * String sessionId = "123456";
	 * 
	 * String view = "web";
	 * String theme = "Limite";
	 * Vector box = new Vector();
	 * int themeType = 0;
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    else
	 *       if(!terraJSP.setTheme(theme, themeType, sessionId))
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("referï¿½ncia"))+".";
	 *    box = terraJSP.getThemeBox(themeType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente ou de referï¿½ncia: {@link #setTheme(String, int, String)}
	 * </pre>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector getThemeBox(int themeType, String restriction,
			String sessionId) throws IllegalAccessException,
			InstantiationException {
		return getThemeBox(themeType, restriction, new Vector(), new Double(0),
				sessionId);
	}

	/**
	 * Mï¿½todo nativo para manipulaï¿½ï¿½o do canvas, que permite ajustar o tamanho,
	 * largura e altura, da ï¿½rea de desenho compativel com o dispositivo de
	 * saï¿½da, tela, e o box da ï¿½rea de interesse, em coordenadas da projeï¿½ï¿½o da
	 * vista corrente. Este mï¿½todo ajusta as coordenadas da ï¿½rea de interesse
	 * para manter a relaï¿½ï¿½o de aspecto da imagem gerada para o dispositivo
	 * conforme os valores definidos para a largura e altura da ï¿½rea de desenho.
	 * O uso deste mï¿½todo ï¿½ prï¿½ requisito nas operaï¿½ï¿½es de desenho de dados
	 * geogrï¿½ficos na ï¿½rea de desenho.<br/>
	 * Atenï¿½ï¿½o: O processo de reconfiguraï¿½ï¿½o do canvas usando este mï¿½todo limpa
	 * a ï¿½rea de desenho, e tudo que foi desenhado atï¿½ o momento serï¿½ perdido.
	 * 
	 * @see <a href="#drawCurrentTheme(java.lang.String)">drawCurrentTheme</a>
	 * @see <a
	 *      href="#drawCurrentThemeLegend(java.lang.String, int, int, boolean, int, java.lang.String)">drawCurrentThemeLegend</a>
	 * @see <a
	 *      href="#drawGroupSqlAndLegend(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, boolean, boolean, boolean, int, int, int, java.lang.String)">drawGroupSqlAndLegend</a>
	 *      e todos os demais que iniciam processo de desenho sobre o canvas,
	 *      mï¿½todos cujo prefixo ï¿½ <b>draw</b>
	 * @see <a
	 *      href="#locateObject(double, double, double, java.lang.String)">locateObject</a>
	 * 
	 * @param xmin
	 *            valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param ymin
	 *            valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param xmax
	 *            valor da longitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param ymax
	 *            valor da latitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param width
	 *            Largura da ï¿½rea de desenho, em pixels, compatï¿½vel com a
	 *            largura da imagem gerada para o dispositivo de visualizaï¿½ï¿½o
	 *            (tela).
	 * @param height
	 *            Altura da ï¿½rea de desenho, em pixels, compatï¿½vel com a altura
	 *            da imagem gerada para o dispositivo de visualizaï¿½ï¿½o (tela).
	 * @param keepAspectRatio
	 * 			  Caso True ajusta as coordenadas da ï¿½rea de interesse
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Um vetor com o box da ï¿½rea de interesse, ajustado conforme a
	 *         largura e altura definidas para a ï¿½rea de desenho (canvas).
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou o box da ï¿½rea de interesse seja invï¿½lido.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * Nï¿½o foi definida uma vista corrente.
	 * Valores xmin, xmax, ymin ou ymax maiores que o valor mï¿½ximo permitido para variï¿½veis tipo float. 
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * int port = 5432;
	 * int dbType = 2;
	 * String sessionId = "123456";
	 * 
	 * String view = "web";
	 * String theme = "Limite";
	 * int themeType = 0;
	 * int width = 800;
	 * int height = 600;
	 * Vector box = new Vector();
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    else
	 *    {
	 *       // recuperando o box da vista corrente.
	 *       box = terraJSP.getCurrentViewBox(sessionId);
	 *       // definindo o box da ï¿½rea de interesse, e o tamanho da ï¿½rea de desenho. 
	 *       box = terraJSP.setWorld(box[0], box[1], box[2], box[3], width, height, sessionId);
	 *    }
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Conhecer um box vï¿½lido, que intercepte o box da vista corrente.
	 * </pre>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector setWorld(double xmin, double ymin, double xmax, double ymax,
			int width, int height, boolean keepAspectRatio, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return setWorld(xmin, ymin, xmax, ymax, width, height, new Vector(),
				new Double(0), keepAspectRatio, sessionId);
	}

	@SuppressWarnings("unchecked")
	private native Vector setWorld(double xmin, double ymin, double xmax,
			double ymax, int width, int height, Vector vectorObj,
			Double doubleObj, boolean keepAspectRatio, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo nativo que permite desenhar na ï¿½rea de desenho, canvas, o conteï¿½do
	 * geogrï¿½fico representado por um tema. Este conteï¿½do pode ser de natureza
	 * vetorial ou matricial. No caso dos dados serem vetoriais a operaï¿½ï¿½o de
	 * desenho considera os estilos na seguinte sequï¿½ncia:
	 * 
	 * <pre>
	 * 1ï¿½ Estilo definido pela camada de aplicaï¿½ï¿½o, atravï¿½s dos mï¿½todos <a href="#setThemeVisualLine(int, int, int, int, int, int, boolean, java.lang.String)">setThemeVisualLine</a>, <a href="#setThemeVisualPoint(int, int, int, int, int, boolean, java.lang.String)">setThemeVisualPoint</a>, <a href="#setThemeVisualPolygon(int, int, int, int, int, int, int, int, int, int, int, boolean, java.lang.String)">setThemeVisualPolygon</a>, <a href="#setThemeVisualText(int, int, int, int, int, int, int, java.lang.String, boolean, java.lang.String)">setThemeVisualText</a>, <a href="#setThemeVisualText(int, int, int, int, int, int, int, java.lang.String, boolean, boolean, double, double, int, int, boolean, java.lang.String)">setThemeVisualText</a>
	 * 2ï¿½ Estilo padrï¿½o definido para o tema, persistido no banco de dados.
	 * </pre>
	 * 
	 * @see <a
	 *      href="#setThemeVisualLine(int, int, int, int, int, int, boolean, java.lang.String)">setThemeVisualLine</a>
	 * @see <a
	 *      href="#setThemeVisualPoint(int, int, int, int, int, boolean, java.lang.String)">setThemeVisualPoint</a>
	 * @see <a
	 *      href="#setThemeVisualPolygon(int, int, int, int, int, int, int, int, int, int, int, boolean, java.lang.String)">setThemeVisualPolygon</a>
	 * @see <a
	 *      href="#setThemeVisualText(int, int, int, int, int, int, int, java.lang.String, boolean, java.lang.String)">setThemeVisualText</a>
	 * @see <a
	 *      href="#setThemeVisualText(int, int, int, int, int, int, int, java.lang.String, boolean, boolean, double, double, int, int, boolean, java.lang.String)">setThemeVisualText</a>
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return uma String com a lista de parï¿½metros da legenda do tema
	 *         desenhado:
	 *         representation-style-R-G-B-transparency-width-ContourStyle
	 *         -ContourR-ContourG-ContourB-ContourWidth-pointsize-from-to-label
	 *         alterar este retorno em futuro prï¿½ximo, retornar a lista em
	 *         estrutura Vector.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos.
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * Uma conexï¿½o nï¿½o foi criada com o banco de dados.
	 * Nï¿½o foi definida uma vista corrente.
	 * Nï¿½o existe um tema definido como corrente.
	 * O canvas nï¿½o foi preparado para iniciar um processo de desenho, @see <a href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * 
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * String sessionId = "123456";
	 * String view = "web";
	 * String theme = "Limite";
	 * int themeType = 0;
	 * int port = 5432;
	 * int dbType = 2;
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    else
	 *    {
	 *       // recuperando o box da vista corrente.
	 *       box = terraJSP.getCurrentViewBox(sessionId);
	 *       // definindo o box da ï¿½rea de interesse, e o tamanho da ï¿½rea de desenho. 
	 *       box = terraJSP.setWorld(box[0], box[1], box[2], box[3], width, height, sessionId);
	 *       // definindo o tema corrente a ser desenhado
	 *       if(!terraJSP.setTheme(theme, themeType, sessionId))
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("referï¿½ncia"))+".";
	 *    }
	 *    // desenhando o conteï¿½do geogrï¿½fico do tema corrente
	 *    terraJSP.drawCurrentTheme(sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente: {@link #setTheme(String, int, String)}
	 * Definir uma ï¿½rea de interesse e um tamanho para a imagem de saï¿½da: {@link #setWorld(double, double, double, double, int, int, String)}
	 * </pre>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public native Vector<HashMap> drawCurrentTheme(String sessionId)
			throws IllegalAccessException, InstantiationException;


	/**
	 * Mï¿½todo nativo que permite gerar uma imagem a partir da ï¿½rea de desenho,
	 * canvas, nos formatos de compressï¿½o PNG, JPEG e GIF. Pode-se solicitar que
	 * o plano de fundo da ï¿½rea de desenho seja transparente ou opaco. A imagem
	 * de saï¿½da serï¿½ uma reproduï¿½ï¿½o fiel do que foi desenhado sobre a ï¿½rea de
	 * desenho, dados vetoriais e matriciais, no tamanho especificado pelo
	 * mï¿½todo setWorld.
	 * 
	 * @see <a
	 *      href="#setWorld(double, double, double, double, int, int, java.lang.String)">setWorld</a>
	 * 
	 * @param imageType
	 *            Tipo de compressï¿½o usada na imagem de saï¿½da.
	 * 
	 *            <pre>
	 * 0: para compressï¿½o PNG.
	 * 1: para compressï¿½o JPEG.
	 * 2: para compressï¿½o GIF.
	 * </pre>
	 * @param isOpaque
	 *            Verdadeiro ou falso definido abaixo, conforme convencionado:
	 * 
	 *            <pre>
	 *  true: gerar imagem de legenda com fundo opaco.
	 *  false: para gerar a imagem de legenda com fundo transparente.
	 * </pre>
	 * @param quality
	 *            Valor numï¿½rico, definido abaixo, que representa a porcentagem
	 *            de qualidade da imagem gerada, caso a saï¿½da seja em formato
	 *            JPEG, conforme convencionado:
	 * 
	 *            <pre>
	 * intervalo vï¿½lido: 0 ~ 100
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Um array de bytes que representa a imagem do mapa desenhado sobre
	 *         a ï¿½rea de desenho para os temas desenhados atï¿½ o momento.
	 * @throws Exception
	 *             Lanï¿½a exceï¿½ï¿½o caso os prï¿½ requisitos nï¿½o tenham sido
	 *             obedecidos ou
	 * 
	 *             <pre>
	 * <b>As causas possï¿½veis sï¿½o:</b>
	 * 
	 * 
	 * 
	 * <b>Exemplo:</b>
	 * <div style="border: 1px dashed #000000;">
	 * String host = "localhost";
	 * String user = "geo";
	 * String password = "secreto";
	 * String base = "banco_geo";
	 * String sessionId = "123456";
	 * String view = "web";
	 * String theme = "Limite";
	 * int themeType = 0;
	 * int port = 5432;
	 * int dbType = 2;
	 * private TerraJSP terraJSP;
	 * String mensagemErro = "";
	 * 
	 * terraJSP = new TerraJSP();
	 * try{
	 *    terraJSP.connect(host, user, password, base, port, dbType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao tentar conectar ao Banco de Dados pela camada JNI: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * try{
	 *    if(!terraJSP.setCurrentView(view, user, sessionId))
	 *       mensagemErro = "Falhou ao configurar a vista "+view+" como corrente.";
	 *    else
	 *       if(!terraJSP.setTheme(theme, themeType, sessionId))
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("referï¿½ncia"))+".";
	 *    theme = terraJSP.getTheme(themeType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente ou de referï¿½ncia: {@link #setTheme(String, int, String)}
	 * </pre>
	 * 
	 */
	public native byte[] getCanvasImage(int imageType, boolean isOpaque,
			int quality, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo nativo que permite salvar em disco uma imagem a partir da ï¿½rea de
	 * desenho, canvas, no formato de compressï¿½o PNG.
	 * 
	 * @param fileName
	 *            Diretï¿½rio onde a imagem deverï¿½ ser salva, caminho completo, e
	 *            nome do arquivo e ser gerado incluindo a extensï¿½o.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) caso tenha sucesso e falso (false) caso
	 *         contrï¿½rio.
	 */
	public native boolean saveCanvasImage(String fileName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Mï¿½todo nativo que permite recuperar a mensagem do ï¿½ltimo erro ocorrido.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Mensagem de erro no formato alfanumï¿½rico, String.
	 */
	public native String errorMessage(String sessionId)
			throws IllegalAccessException, InstantiationException;

	@SuppressWarnings("unchecked")
	private native Vector getViews(String user, Vector vectorObj,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo nativo que permite recuperar a lista de vistas existente no banco
	 * de dados ao qual se estï¿½ conectado e que tenham sido criadas pelo usuï¿½rio
	 * especificado pelo parï¿½metro user.
	 * 
	 * @param user
	 *            Nome de um usuï¿½rio vï¿½lido com permissï¿½es de acesso ao banco de
	 *            dados.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Lista de vistas do usuï¿½rio especificado, em estrutura Vector.
	 */
	@SuppressWarnings("unchecked")
	public Vector getViews(String user, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return getViews(user, new Vector(), sessionId);
	}

	/**
	 * Mï¿½todo nativo que permite recuperar a imagem da legenda no formato PNG,
	 * desenhada pelo mï¿½todo drawLegend.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Array de bytes que representa a imagem da legenda.
	 */
	public native byte[] getLegendImage(int imageType, boolean isOpaque,
			int quality, String sessionId) throws IllegalAccessException,
			InstantiationException;

	@SuppressWarnings("unchecked")
	private native Vector getCurrentViewBox(Vector vectorObj, Double doubleObj,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Retorna um vetor de quatro elementos float contendo as coordenadas do
	 * retï¿½ngulo envolvente dos objetos de todos os temas da vista corrente (x1,
	 * y1, x2 e y2 respectivamente). Os valores das coordenadas encontram-se no
	 * sistema de projeï¿½ï¿½o indicada pela vista corrente.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Lista de valores Double em estrutura Vector (x1, y1, x2 e y2
	 *         respectivamente).
	 */
	@SuppressWarnings("unchecked")
	public Vector getCurrentViewBox(String sessionId)
			throws IllegalAccessException, InstantiationException {
		return getCurrentViewBox(new Vector(), new Double(0), sessionId);
	}


	/**
	 * Desenha um retï¿½ngulo sobre o canvas com possibilidade de ser preenchido
	 * ou nï¿½o. As coordenadas do retï¿½ngulo devem estar no sistema de projeï¿½ï¿½o da
	 * vista corrente.
	 * 
	 * @param x1
	 *            valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y1
	 *            valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param x2
	 *            valor da longitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y2
	 *            valor da latitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param fill
	 *            Define se o retï¿½ngulo deve ser preenchido ou nï¿½o.
	 * 
	 *            <pre>
	 * 0: sem preenchimento.
	 * 1: com preenchimento.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel desenhar o retï¿½ngulo solicitado
	 *         e falso (false) caso contrï¿½rio.
	 */
	private native boolean drawBox(double x1, double y1, double x2, double y2,
			int fill, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Desenha um retï¿½ngulo sobre o canvas sem preenchimento. As coordenadas do
	 * retï¿½ngulo devem estar no sistema de projeï¿½ï¿½o da vista corrente.
	 * 
	 * @param x1
	 *            valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y1
	 *            valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param x2
	 *            valor da longitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y2
	 *            valor da latitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel desenhar o retï¿½ngulo solicitado
	 *         e falso (false) caso contrï¿½rio.
	 */
	public boolean drawBox(double x1, double y1, double x2, double y2,
			String sessionId) throws IllegalAccessException,
			InstantiationException {
		return drawBox(x1, y1, x2, y2, 0, sessionId);
	}

	/**
	 * Desenha rï¿½tulo de texto sobre geometrias com representaï¿½ï¿½o de linha no
	 * ï¿½ngulo da tangente gerada no ponto de desenho do texto com o seguimento
	 * de linha e o eixo das ordenadas. O rï¿½tulo ï¿½ extraï¿½do de uma das colunas
	 * da tabela de atributos. A operaï¿½ï¿½o de desenho pode executar um algoritmo
	 * de verificaï¿½ï¿½o de conflito. O contexto para a geraï¿½ï¿½o da camada de texto
	 * sobre o mapa ï¿½ configurado pelos mï¿½todos:
	 * 
	 * @see <a
	 *      href="#setTextOutLineEnable(boolean, java.lang.String)">setTextOutLineEnable</a>
	 * @see <a
	 *      href="#setTextOutLineColor(int, int, int, java.lang.String)">setTextOutLineColor</a>
	 * @see <a
	 *      href="#setLabelField(java.lang.String, java.lang.String)">setLabelField</a>
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, java.lang.String)">setDefaultVisual</a>
	 * @see <a
	 *      href="#setPriorityField(java.lang.String, java.lang.String)">setPriorityField</a>
	 * @see <a
	 *      href="#setMinCollisionTolerance(int, java.lang.String)">setMinCollisionTolerance</a>
	 * @see <a
	 *      href="#setConflictDetect(boolean, java.lang.String)">setConflictDetect</a>
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel desenhar os rï¿½tulos e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean drawLineAngleTextLabeling(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Desenha rï¿½tulo de texto sobre geometrias com de qualquer representaï¿½ï¿½o na
	 * horizontal. O rï¿½tulo ï¿½ extraï¿½do de uma das colunas da tabela de
	 * atributos. A operaï¿½ï¿½o de desenho pode executar um algoritmo de
	 * verificaï¿½ï¿½o de conflito. O contexto para a geraï¿½ï¿½o da camada de texto
	 * sobre o mapa ï¿½ configurado pelos mï¿½todos:
	 * 
	 * @see <a
	 *      href="#setTextOutLineEnable(boolean, java.lang.String)">setTextOutLineEnable</a>
	 * @see <a
	 *      href="#setTextOutLineColor(int, int, int, java.lang.String)">setTextOutLineColor</a>
	 * @see <a
	 *      href="#setLabelField(java.lang.String, java.lang.String)">setLabelField</a>
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, java.lang.String)">setDefaultVisual</a>
	 * @see <a
	 *      href="#setPriorityField(java.lang.String, java.lang.String)">setPriorityField</a>
	 * @see <a
	 *      href="#setMinCollisionTolerance(int, java.lang.String)">setMinCollisionTolerance</a>
	 * @see <a
	 *      href="#setConflictDetect(boolean, java.lang.String)">setConflictDetect</a>
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel desenhar os rï¿½tulos e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean drawHorizontalTextLabeling(String restrExp, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	public boolean drawHorizontalTextLabeling(String sessionId) 
			throws IllegalAccessException, InstantiationException {
		return drawHorizontalTextLabeling("", sessionId);
	}

	/**
	 * Retorna as representaï¿½ï¿½es ativas do tema corrente ou do tema de
	 * referï¿½ncia.
	 * 
	 * @return Valor numï¿½rico inteiro conforme a lista:
	 * 
	 *         <pre>
	 * 1 Polï¿½gonos
	 * 2 Linhas
	 * 4 Pontos
	 * 128 Texto
	 * 256 Cï¿½lulas
	 * 512 Raster.
	 * </pre>
	 */
	public native int getThemeRepresentation(int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Dado um identificador de objeto ou lista de identificadores de objeto de
	 * um tema, gera um mapa de distï¿½ncia ao redor do(s) objeto(s) indicado(s) e
	 * desenha-os em seguida.
	 * 
	 * @param Vector
	 *            <String> Oids, Vetor de objectIds do tipo: java/lang/String
	 * @param double distance, ajuste de distï¿½ncia para a criaï¿½ï¿½o do buffer em
	 *        torno da(s) geometrias(s) encontradas para os identificadores em
	 *        Oids. O valor zero (distance=0) nï¿½o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precisï¿½o, e MENOR o desempenho. Usado
	 *        apenas na geraï¿½ï¿½o de buffer.
	 * @param boolean unionPoly, executar uniï¿½o dos poligonos de entrada com os
	 *        buffers
	 * @param int themeType, tema corrente ou de referï¿½ncia.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return true caso desenhe com sucesso e false caso contrï¿½rio.
	 * 
	 **/
	public native boolean drawBufferZoneWithOids(Vector<String> Oids,
			double distance, int bufferType, int numPoints, boolean unionPoly,
			int themeType, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado um identificador de objeto ou lista de identificadores de objeto de
	 * um tema, gera um mapa de distï¿½ncia ao redor do(s) objeto(s) indicado(s) e
	 * desenha-os em seguida.
	 * 
	 * @param Vector
	 *            <Point2D.Double> points, Vetor de pontos do tipo:
	 *            java/awt/geom/Point2D.Double
	 * @param double distance, ajuste de distï¿½ncia para a criaï¿½ï¿½o do buffer em
	 *        torno da(s) geometrias(s) encontradas para os identificadores em
	 *        Oids. O valor zero (distance=0) nï¿½o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precisï¿½o, e MENOR o desempenho. Usado
	 *        apenas na geraï¿½ï¿½o de buffer.
	 * @param boolean unionPoly, executar uniï¿½o dos poligonos de entrada com os
	 *        buffers
	 * @param int themeType, tema corrente ou de referï¿½ncia.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return true caso desenhe com sucesso e false caso contrï¿½rio.
	 * 
	 **/
	public native boolean drawBufferZoneWithPoints(
			Vector<Point2D.Double> points, double distance, int bufferType,
			int numPoints, boolean unionPoly, int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	@SuppressWarnings("unchecked")
	private native Vector getThemes(Vector vectorObj, boolean onlyVisible,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo de acesso aos nome dos temas da vista corrente.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Lista de nomes dos temas da vista corrente em formato Vector.
	 */
	@SuppressWarnings("unchecked")
	public Vector getThemes(String sessionId) throws IllegalAccessException,
			InstantiationException {
		return getThemes(new Vector(), false, sessionId);
	}

	/**
	 * Consulta por apontamento. Retorna o identificador do objeto que contï¿½m um
	 * determinado ponto. Em operaï¿½ï¿½es de localizaï¿½ï¿½o de objetos geogrï¿½ficos
	 * vetoriais pela funï¿½ï¿½o locateObject, o calculo da tolerancia ï¿½ ajustado
	 * conforme o box da ï¿½rea de interesse em relaï¿½ï¿½o ao tamanho da imagem de
	 * saï¿½da do dispositivo de visualizaï¿½ï¿½o, tornando impresindï¿½vel o ajuste da
	 * ï¿½rea de desenho, canvas, com o box da ï¿½rea de interesse atravï¿½s do uso do
	 * mï¿½todo setWorld().
	 */
	@SuppressWarnings("unchecked")
	public native Vector locateObject(double x, double y, double tol,
			int themeType, boolean storeGeom, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Retorna o identificador de um objeto que contï¿½m um determinado ponto,
	 * levando em consideraï¿½ï¿½o uma tolerï¿½ncia em pixel. Pode operar sobre o tema
	 * corrente ou de referï¿½ncia. Geralmente usado em consulta por apontamento,
	 * onde temos um ponto sobre a ï¿½rea de interesse. Em operaï¿½ï¿½es de
	 * localizaï¿½ï¿½o de objetos geogrï¿½ficos vetoriais pela funï¿½ï¿½o locateObject, o
	 * calculo da tolerancia ï¿½ ajustado conforme o box da ï¿½rea de interesse em
	 * relaï¿½ï¿½o ao tamanho da imagem de saï¿½da do dispositivo de visualizaï¿½ï¿½o,
	 * tornando impresindï¿½vel o ajuste da ï¿½rea de desenho, canvas, com o box da
	 * ï¿½rea de interesse atravï¿½s do uso do mï¿½todo setWorld().
	 * 
	 * @param x
	 *            Valor da coordenada do ponto de interesse, longitude, no
	 *            sistema de projeï¿½ï¿½es da vista corrente.
	 * @param y
	 *            Valor da coordenada do ponto de interesse, latitude, no
	 *            sistema de projeï¿½ï¿½es da vista corrente.
	 * @param tol
	 *            Valor da tolerï¿½ncia requisitada para operaï¿½ï¿½o de cruzamento
	 *            entre o ponto solicitado e as geometrias presentes na camada
	 *            pesquisada, representadas pelo tema corrente ou de referï¿½ncia.
	 * @param themeType
	 *            Definiï¿½ï¿½o de tema a ser pesquisado, corrente ou de referï¿½ncia.
	 * 
	 *            <pre>
	 * 0: tema corrente.
	 * 1: tema de referï¿½ncia.
	 * </pre>
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Valores em formato Vector, [0]=valorObjectId e [1]=valorGeomId
	 */
	@SuppressWarnings("unchecked")
	public Vector locateObject(double x, double y, double tol, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException {
		return locateObject(x, y, tol, themeType, false, sessionId);
	}

	@SuppressWarnings("unchecked")
	public Vector locateObject(double x, double y, double tol, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return locateObject(x, y, tol, 0, sessionId);
	}

	@SuppressWarnings("unchecked")
	public Vector locateObject(double x, double y, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return locateObject(x, y, 0.0, 0, sessionId);
	}

	/**
	 * Dado um identificadore de objeto ou lista de identificadores de objeto do
	 * tema corrente, retorna a lista de identificadores de objeto que
	 * satisfaï¿½am um certo relacionamento topolï¿½gico sobre as geometrias do tema
	 * referï¿½ncia.
	 * 
	 * @param Vector
	 *            <String> Oids, Vetor de objectIds do tipo: java/lang/String
	 * @param int relation, relacionamento topolï¿½gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrepï¿½em 32 Contï¿½m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de distï¿½ncia para a criaï¿½ï¿½o do buffer em
	 *        torno da(s) geometrias(s) encontradas para os identificadores em
	 *        Oids. O valor zero (distance=0) nï¿½o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precisï¿½o, e MENOR o desempenho. Usado
	 *        apenas na geraï¿½ï¿½o de buffer.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         operaï¿½ï¿½o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithOids(Vector<String> Oids,
			int relation, double distance, int bufferType, int numPoints,
			boolean unionPoly, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado um ponto ou lista de pontos, retorna a lista de objetos
	 * (identificadores) que satisfaï¿½am um certo relacionamento topolï¿½gico sobre
	 * as geometrias do tema corrente.
	 * 
	 * @param Vector
	 *            <Point2D.Double> aListPoints, Vetor de pontos do tipo:
	 *            java/awt/geom/Point2D.Double
	 * @param int relation, relacionamento topolï¿½gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrepï¿½em 32 Contï¿½m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de distï¿½ncia para a criaï¿½ï¿½o do buffer em
	 *        torno do(s) ponto(s) em aListPoints. O valor zero (distance=0) nï¿½o
	 *        gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precisï¿½o, e MENOR o desempenho. Usado
	 *        apenas na geraï¿½ï¿½o de buffer.
	 * @param int themeType, indicaï¿½ï¿½o do tema de pesquisa, corrente ou
	 *        referï¿½ncia:
	 * 
	 *        0: tema corrente. 1: tema de referï¿½ncia.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         operaï¿½ï¿½o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithPoints(
			Vector<Point2D.Double> aListPoints, int relation, double distance,
			int bufferType, int numPoints, int themeType, boolean unionPoly,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado uma linha ou lista de linhas, retorna a lista de objetos
	 * (identificadores) que satisfaï¿½am um certo relacionamento topolï¿½gico sobre
	 * as geometrias do tema corrente.
	 * 
	 * @param Vector
	 *            <Vector<Point2D.Double>> aListLines, Vetor de pontos do tipo:
	 *            java/awt/geom/Point2D.Double
	 * @param int relation, relacionamento topolï¿½gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrepï¿½em 32 Contï¿½m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de distï¿½ncia para a criaï¿½ï¿½o do buffer em
	 *        torno da(s) linha(s) em aListLines. O valor zero (distance=0) nï¿½o
	 *        gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precisï¿½o, e MENOR o desempenho. Usado
	 *        apenas na geraï¿½ï¿½o de buffer.
	 * @param int themeType, indicaï¿½ï¿½o do tema de pesquisa, corrente ou
	 *        referï¿½ncia:
	 * 
	 *        0: tema corrente. 1: tema de referï¿½ncia.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         operaï¿½ï¿½o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithLines(
			Vector<Vector<Point2D.Double>> aListLines, int relation,
			double distance, int bufferType, int numPoints, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado uma linha ou lista de linhas, retorna a lista de objetos
	 * (identificadores) que satisfaï¿½am um certo relacionamento topolï¿½gico sobre
	 * as geometrias do tema corrente.
	 * 
	 * @param Vector
	 *            <Vector<Vector<Point2D.Double>>> aListPolygons, Vetor de
	 *            polygons do tipo: java/awt/geom/Point2D.Double
	 * @param int relation, relacionamento topolï¿½gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrepï¿½em 32 Contï¿½m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de distï¿½ncia para a criaï¿½ï¿½o do buffer em
	 *        torno do(s) polygon(s) em aListPolygons. O valor zero (distance=0)
	 *        nï¿½o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precisï¿½o, e MENOR o desempenho. Usado
	 *        apenas na geraï¿½ï¿½o de buffer.
	 * @param int themeType, indicaï¿½ï¿½o do tema de pesquisa, corrente ou
	 *        referï¿½ncia:
	 * 
	 *        0: tema corrente. 1: tema de referï¿½ncia.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         operaï¿½ï¿½o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithPolygons(
			Vector<Vector<Vector<Point2D.Double>>> aListPolygons, int relation,
			double distance, int bufferType, int numPoints, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado um identificador de um objeto do tema corrente, retorna a lista de
	 * objetos (identificadores) do tema de referï¿½ncia que satisfaï¿½am um certo
	 * relacionamento topolï¿½gico (toca, cruza, sobrepï¿½e, dentro, contï¿½m,
	 * disjunto) com ele.
	 **/
	@SuppressWarnings("unchecked")
	private native Vector locateObjectsUsingRefTheme(String objid,
			int relation, Vector vectorObj, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Retorna a lista de atributos de um dado objeto.
	 **/
	@SuppressWarnings("unchecked")
	private native Vector fetchAttributes(String objectid, int themeType,
			Vector vectorObj, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo nativo que permite recuperar a lista de atributos de uma geometria
	 * do tema carregado como corrente ou de referï¿½ncia.
	 * 
	 * @param objectid
	 *            o identificador ï¿½nico da geometria para a qual se deseja ler a
	 *            lista de atributos e seus respectivos valores.
	 * @param themeType
	 *            indicaï¿½ï¿½o do tema onde encontrar a geometria indicada pelo
	 *            parï¿½metro objectid, corrente ou referï¿½ncia:
	 * 
	 *            <pre>
	 * 0: tema corrente.
	 * 1: tema de referï¿½ncia.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Lista dos atributos relacioandos para a geometria indicada, em
	 *         estrutura de dados tipo Vector, padronizado em:
	 * 
	 *         <pre>
	 * [0]=nome Da Coluna da tabela de atributos.
	 * [1]=valor do registro para a coluna indicada na posiï¿½ï¿½o anterior.
	 * [2]=nome Da Coluna da tabela de atributos.
	 * [3]=valor do registro para a coluna indicada na posiï¿½ï¿½o anterior.
	 * e assim sucessivamente.
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public Vector fetchAttributes(String objectid, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException {
		return fetchAttributes(objectid, themeType, new Vector(), sessionId);
	}

	/**
	 * Ajusta o visual de uma determinada representaï¿½ï¿½o geomï¿½trica a ser
	 * desenhada. Atua apenas em tempo de execuï¿½ï¿½o. Os seguintes mï¿½todos
	 * utilizam este visual: drawSelectedObject, drawPoint, drawBox, drawTex,
	 * drawBufferZone e drawLegend.
	 * 
	 * @param rep
	 *            Representaï¿½ï¿½o geomï¿½trica que receberï¿½ o novo visual comforme
	 *            definiï¿½ï¿½o TerraLib: cï¿½lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: cï¿½lulas
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria.
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria.
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria.
	 * @param style
	 *            Estilo para representaï¿½ï¿½o geomï¿½trica.
	 * 
	 *            <pre>
	 * Ponto, linha ou poligono, observar o convencionado pelo TerraLib:
	 * 
	 * <b>Preenchimento cï¿½lula ou poligono</b>: 0 = transparente, 1 = preenchimento opaco, 2 = hachura horizontal, 3 = hachura vertical,
	 * 4 = hachura diagonal inclinaï¿½ï¿½o em 135ï¿½, 5 = hachura diagonal inclinaï¿½ï¿½o em 45ï¿½,
	 * 6 = hachura horizontal e vertical, 7 = hachura horizontal e vertical inclinada em 45ï¿½
	 * <b>linha ou contorno de poligonos</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = traï¿½o ponto, 4 = traï¿½o ponto ponto
	 * <b>ponto</b>: type 1 = estrela, 2 = circulo, 3 = X, 4 = quadrado, 5 = diamante, 6 = circulo vazado, 7 = quadrado vazado, 8 = diamente vazado
	 * <b>texto</b>: define-se a fonte o tamanho e a cor.
	 * </pre>
	 * @param width
	 *            dimensï¿½o do objeto.
	 * @param rcontour
	 *            Componente vermelha da cor de contorno da geometria.
	 * @param gcontour
	 *            Componente verde da cor de contorno da geometria.
	 * @param bcontour
	 *            Componente azul da cor de contorno da geometria.
	 * @param stylecontour
	 *            Estilo do contorno, adotar estilo de linhas.
	 * @param widthcontour
	 *            Espessura do contorno.
	 * @param fontName
	 *            usada no caso de representaï¿½ï¿½o de texto, nome da fonte
	 *            incluindo o caminho completo para acessa-lï¿½. Pode ser
	 *            necessï¿½rio verificar as permissï¿½es de acesso ao aqruivo de
	 *            fontes.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel configurar o estilo e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean setDefaultVisual(int rep, int red, int green,
			int blue, int style, int width, String fontName, int rcontour,
			int gcontour, int bcontour, int stylecontour, int widthcontour,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Ajusta o visual de uma determinada representaï¿½ï¿½o geomï¿½trica a ser
	 * desenhada. ï¿½ a segunda na ordem de prioridade sobre as demais definiï¿½ï¿½es
	 * de visual, sendo a SLD a primeira. Atua apenas em tempo de execuï¿½ï¿½o, nï¿½o
	 * persiste em banco. Os seguintes mï¿½todos utilizam este visual:
	 * drawSelectedObject, drawPoint, drawBox, drawTex, drawBufferZone e
	 * drawLegend.
	 * 
	 * @param rep
	 *            Representaï¿½ï¿½o geomï¿½trica que receberï¿½ o novo visual comforme
	 *            definiï¿½ï¿½o TerraLib: cï¿½lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: cï¿½lulas
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria.
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria.
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria.
	 * @param style
	 *            Estilo para representaï¿½ï¿½o geomï¿½trica.
	 * 
	 *            <pre>
	 * Ponto, linha ou poligono, observar o convencionado pelo TerraLib:
	 * 
	 * <b>Preenchimento cï¿½lula ou poligono</b>: 0 = transparente, 1 = preenchimento opaco, 2 = hachura horizontal, 3 = hachura vertical,
	 * 4 = hachura diagonal inclinaï¿½ï¿½o em 135ï¿½, 5 = hachura diagonal inclinaï¿½ï¿½o em 45ï¿½,
	 * 6 = hachura horizontal e vertical, 7 = hachura horizontal e vertical inclinada em 45ï¿½
	 * <b>linha ou contorno de poligonos</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = traï¿½o ponto, 4 = traï¿½o ponto ponto
	 * <b>ponto</b>: 1 = estrela, 2 = circulo, 3 = X, 4 = quadrado, 5 = diamante, 6 = circulo vazado, 7 = quadrado vazado, 8 = diamente vazado
	 * <b>texto</b>: define-se a fonte o tamanho e a cor.
	 * </pre>
	 * @param width
	 *            dimensï¿½o do objeto.
	 * @param fontName
	 *            usada no caso de representaï¿½ï¿½o de texto, nome da fonte
	 *            incluindo o caminho completo para acessa-lï¿½. Pode ser
	 *            necessï¿½rio verificar as permissï¿½es de acesso ao aqruivo de
	 *            fontes.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel configurar o estilo e falso
	 *         (false) caso contrï¿½rio.
	 */
	public boolean setDefaultVisual(int rep, int red, int green, int blue,
			int style, int width, String fontName, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return setDefaultVisual(rep, red, green, blue, style, width, fontName,
				0, 0, 0, 0, 0, sessionId);
	}

	/**
	 * Recupera o visual padrï¿½o, definido para um conjunto de objetos de uma
	 * representaï¿½ï¿½o a serem desenhados individualmente. ï¿½ a segunda na ordem de
	 * prioridade sobre as demais definiï¿½ï¿½es de visual, sendo a SLD a primeira.
	 * Atenï¿½ï¿½o: Este visual padrï¿½o ï¿½ armazenado em memï¿½ria apenas e portanto
	 * volï¿½til.
	 * 
	 * @param rep
	 *            Representaï¿½ï¿½o geomï¿½trica que receberï¿½ o novo visual comforme
	 *            definiï¿½ï¿½o TerraLib: cï¿½lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: cï¿½lulas
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Lista dos parï¿½metros referï¿½ntes ao visual de uma representaï¿½ï¿½o
	 *         geomï¿½trica, conforme definiï¿½ï¿½es em
	 *         {@link #setDefaultVisual(int, int, int, int, int, int, String, int, int, int, int, int, String)}
	 *         e
	 *         {@link #setDefaultVisual(int, int, int, int, int, int, String, String)}
	 */
	@SuppressWarnings("unchecked")
	public native Vector getDefaultVisual(int rep, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Recupera o visual padrï¿½o, definido para uma representaï¿½ï¿½o do tema
	 * corrente. Atenï¿½ï¿½o: Este ï¿½ o visual padrï¿½o armazenado no banco e definido
	 * para um tema corrente.
	 * 
	 * @param rep
	 *            Representaï¿½ï¿½o geomï¿½trica que receberï¿½ o novo visual comforme
	 *            definiï¿½ï¿½o TerraLib: cï¿½lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: cï¿½lulas
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Lista dos parï¿½metros referï¿½ntes ao visual de uma representaï¿½ï¿½o
	 *         geomï¿½trica.
	 */
	@SuppressWarnings("unchecked")
	public native Vector getThemeVisual(int rep, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite configurar a apresentaï¿½ï¿½o de geometrias do tipo poligonos,
	 * baseado nos parï¿½metros de visual disponï¿½veis na TerraLib. O parï¿½metro
	 * persistence permite gravar as configuraï¿½ï¿½es no banco de dados, alterando
	 * definitivamente o visual padrï¿½o para desenho de poligonos.
	 * 
	 * @param styleId
	 *            Estilo para representaï¿½ï¿½o geomï¿½trica.
	 * 
	 *            <pre>
	 * Poligono, observar o convencionado pelo TerraLib:
	 * 
	 * <b>Preenchimento cï¿½lula ou poligono</b>: 0 = transparente, 1 = preenchimento opaco, 2 = hachura horizontal, 3 = hachura vertical,
	 * 4 = hachura diagonal inclinaï¿½ï¿½o em 135ï¿½, 5 = hachura diagonal inclinaï¿½ï¿½o em 45ï¿½,
	 * 6 = hachura horizontal e vertical, 7 = hachura horizontal e vertical inclinada em 45ï¿½
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria,
	 *            valores vï¿½lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param transparency
	 *            Cor de preenchimento aceita valores no intervalo (0 - 100),
	 *            medida de porcentagem, para aplicar nï¿½vel de transparï¿½ncia.
	 * @param contourStyleId
	 *            Considerar os estilos possï¿½veis para linha:
	 * 
	 *            <pre>
	 * <b>Contorno de poligonos</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = traï¿½o ponto, 4 = traï¿½o ponto ponto
	 * </pre>
	 * @param redContour
	 *            Componente vermelha da cor de contorno da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param greenContour
	 *            Componente verde da cor de contorno da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blueContour
	 *            Componente azul da cor de contorno da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param transparencyContour
	 *            Cor da linha de contorno aceita valores no intervalo (0 -
	 *            100), medida de porcentagem, para aplicar nï¿½vel de
	 *            transparï¿½ncia.
	 * @param widthContour
	 *            Largura da linha de contorno do poligono.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso nï¿½o armazena, mantï¿½m na
	 *            memï¿½ria, e ï¿½ perdido quando outro tema corrente ï¿½ definido.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel configurar o estilo e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean setThemeVisualPolygon(int styleId, int red,
			int green, int blue, int transparency, int contourStyleId,
			int redContour, int greenContour, int blueContour,
			int transparencyContour, int widthContour, boolean persistence,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite configurar a apresentaï¿½ï¿½o de geometrias do tipo linha, baseado
	 * nos parï¿½metros de visual disponï¿½veis na TerraLib. O parï¿½metro persistence
	 * permite gravar as configuraï¿½ï¿½es no banco de dados, alterando
	 * definitivamente o visual padrï¿½o para desenho de linhas.
	 * 
	 * @param styleId
	 *            Estilo para representaï¿½ï¿½o geomï¿½trica.
	 * 
	 *            <pre>
	 * Linha, observar o convencionado pelo TerraLib:
	 * <b>Linhas</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = traï¿½o ponto, 4 = traï¿½o ponto ponto
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria,
	 *            valores vï¿½lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param transparency
	 *            Cor de preenchimento aceita valores no intervalo (0 - 100),
	 *            medida de porcentagem, para aplicar nï¿½vel de transparï¿½ncia.
	 * @param width
	 *            Largura da linha.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso nï¿½o armazena, mantï¿½m na
	 *            memï¿½ria, e ï¿½ perdido quando outro tema corrente ï¿½ definido.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel configurar o estilo e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean setThemeVisualLine(int styleId, int red, int green,
			int blue, int transparency, int width, boolean persistence,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite configurar a apresentaï¿½ï¿½o de geometrias do tipo ponto, baseado
	 * nos parï¿½metros de visual disponï¿½veis na TerraLib. O parï¿½metro persistence
	 * permite gravar as configuraï¿½ï¿½es no banco de dados, alterando
	 * definitivamente o visual padrï¿½o para desenho de pontos.
	 * 
	 * @param styleId
	 *            Estilo para representaï¿½ï¿½o geomï¿½trica.
	 * 
	 *            <pre>
	 * Ponto, observar o convencionado pelo TerraLib:
	 * <b>Pontos</b>: 1 = estrela, 2 = circulo, 3 = X, 4 = quadrado, 5 = diamante, 6 = circulo vazado, 7 = quadrado vazado, 8 = diamente vazado
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria,
	 *            valores vï¿½lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param size
	 *            Tamanho do ponto.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso nï¿½o armazena, mantï¿½m na
	 *            memï¿½ria, e ï¿½ perdido quando outro tema corrente ï¿½ definido.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel configurar o estilo e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean setThemeVisualPoint(int styleId, int red, int green,
			int blue, int size, boolean persistence, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * 
	 * Ver a assinatura simplificada de setThemeVisual, a qual pressupï¿½e valores
	 * padrï¿½o para os atributos que ainda nï¿½o tem suporte habilitado na camada
	 * de desenho, definidos para uso futuro. Parï¿½metros omitidos: boolean bold,
	 * boolean italic, double alignmentVert, double alignmentHoriz, int tabSize,
	 * int lineSpace
	 * ------------------------------------------------------------
	 * ----------------------------------- Permite configurar a apresentaï¿½ï¿½o de
	 * geometrias do tipo texto, baseado nos parï¿½metros de visual disponï¿½veis na
	 * TerraLib. O parï¿½metro persistence permite gravar as configuraï¿½ï¿½es no
	 * banco de dados, alterando definitivamente o visual padrï¿½o para desenho de
	 * texto. ATENï¿½ï¿½O: Altera o visual de representaï¿½ï¿½es geomï¿½tricas do tipo
	 * texto (criados pelo TerraView por exemplo), e nï¿½o os textos desenhados
	 * dinï¿½micamente.
	 * 
	 * @param red
	 *            Componente vermelha da cor de preenchimento do caracter,
	 *            valores vï¿½lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param redContour
	 *            Componente vermelha da cor de contorno do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param greenContour
	 *            Componente verde da cor de contorno do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blueContour
	 *            Componente azul da cor de contorno do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param size
	 *            Tamanho do texto.
	 * @param familyPath
	 *            Nome do arquivo de fonte e diretï¿½rio onde o arquivo se
	 *            encontra, caminho completo. Pode ser necessï¿½rio ajustar a
	 *            permissï¿½o de acesso a leitura do arquivo.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso nï¿½o armazena, mantï¿½m na
	 *            memï¿½ria, e ï¿½ perdido quando outro tema corrente ï¿½ definido.
	 * 
	 *            <pre>
	 * true = armazena no banco de dados.
	 * false = armazena apenas na memï¿½ria.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel configurar o estilo e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean setThemeVisualText(int red, int green, int blue,
			int redContour, int greenContour, int blueContour, int size,
			String familyPath, boolean bold, boolean italic,
			double alignmentVert, double alignmentHoriz, int tabSize,
			int lineSpace, boolean persistence, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite configurar a apresentaï¿½ï¿½o de geometrias do tipo texto, baseado
	 * nos parï¿½metros de visual disponï¿½veis na TerraLib. O parï¿½metro persistence
	 * permite gravar as configuraï¿½ï¿½es no banco de dados, alterando
	 * definitivamente o visual padrï¿½o para desenho de texto. ATENï¿½ï¿½O: Altera o
	 * visual de representaï¿½ï¿½es geomï¿½tricas do tipo texto (criados pelo
	 * TerraView por exemplo), e nï¿½o os textos desenhados dinï¿½micamente.
	 * 
	 * @param red
	 *            Componente vermelha da cor de preenchimento do caracter,
	 *            valores vï¿½lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param redContour
	 *            Componente vermelha da cor de contorno do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param greenContour
	 *            Componente verde da cor de contorno do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param blueContour
	 *            Componente azul da cor de contorno do caracter, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param size
	 *            Tamanho do texto.
	 * @param familyPath
	 *            Nome do arquivo de fonte e diretï¿½rio onde o arquivo se
	 *            encontra, caminho completo. Pode ser necessï¿½rio ajustar a
	 *            permissï¿½o de acesso a leitura do arquivo.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso nï¿½o armazena, mantï¿½m na
	 *            memï¿½ria, e ï¿½ perdido quando outro tema corrente ï¿½ definido.
	 * 
	 *            <pre>
	 * true = armazena no banco de dados.
	 * false = armazena apenas na memï¿½ria.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel configurar o estilo e falso
	 *         (false) caso contrï¿½rio.
	 */
	public boolean setThemeVisualText(int red, int green, int blue,
			int redContour, int greenContour, int blueContour, int size,
			String familyPath, boolean persistence, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return setThemeVisualText(red, green, blue, redContour, greenContour,
				blueContour, size, familyPath, false, false, 0.0, 0.0, 0, 0,
				persistence, sessionId);
	}

	/**
	 * Desenha no canvas um conjunto de objetos indicados por uma lista de
	 * identificadores (object_id). Somente a representaï¿½ï¿½o ativa (do tema
	 * corrente ou do tema de referï¿½ncia) serï¿½o desenhados. Este mï¿½todo sempre
	 * considera o visual definido por setDefaultVisual.
	 * 
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, int, int, int, int, int, java.lang.String)">setDefaultVisual</a>
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, java.lang.String)">setDefaultVisual</a>
	 * @param objArray
	 *            Lista de identificadores de geometrias vï¿½lidas para o tema
	 *            corrente ou de referï¿½ncia.
	 * @param themeType
	 *            Se vai operar sobre o tema corrente ou de referï¿½ncia:
	 * 
	 *            <pre>
	 * 0: tema corrente.
	 * 1: tema de referï¿½ncia.
	 * </pre>
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel desenhar as geometrias
	 *         solicitadas na lista e falso (false) caso contrï¿½rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean drawSelectedObjects(Vector objArray, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Desenha no canvas um conjunto de objetos indicados por uma lista de
	 * identificadores (object_id). Somente a representaï¿½ï¿½o ativa do tema
	 * corrente serï¿½o desenhados. Este mï¿½todo ï¿½ uma simplificaï¿½ï¿½o do
	 * {@link #drawSelectedObjects(Vector, int, String)} Este mï¿½todo sempre
	 * considera o visual definido por setDefaultVisual.
	 * 
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, int, int, int, int, int, java.lang.String)">setDefaultVisual</a>
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, java.lang.String)">setDefaultVisual</a>
	 * @param objArray
	 *            Lista de identificadores de geometrias vï¿½lidas para o tema
	 *            corrente ou de referï¿½ncia.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return Verdadeiro (true) se foi possï¿½vel desenhar as geometrias
	 *         solicitadas na lista e falso (false) caso contrï¿½rio.
	 */
	@SuppressWarnings("unchecked")
	public boolean drawSelectedObjects(Vector objArray, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return drawSelectedObjects(objArray, 0, sessionId);
	}

	/**
	 * Desenha uma legenda em um canvas auxiliar. Essa ï¿½rea serï¿½ automaticamente
	 * apagada quando uma nova chamada a este mï¿½todo for realizada. A imagem
	 * criada deverï¿½ ser recuperada atravï¿½s do mï¿½todo getLegendImage.
	 * 
	 * @param legends
	 *            Lista de parï¿½metros que representa uma legenda. Esta lista ï¿½
	 *            em formato String, e deve ser o retorno do mï¿½todo
	 *            drawCurrentTheme.
	 * @param width
	 *            Largura da legenda gerada.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */

	public native void drawLegend(String legends, int width, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Desenha uma legenda em um canvas auxiliar. Essa ï¿½rea serï¿½ automaticamente
	 * apagada quando uma nova chamada a este mï¿½todo for realizada. A imagem
	 * criada deverï¿½ ser recuperada atravï¿½s do mï¿½todo getLegendImage.
	 * 
	 * @param legends
	 *            Lista de parï¿½metros que representa uma legenda. Esta lista ï¿½
	 *            em formato String, e deve ser o retorno do mï¿½todo
	 *            drawCurrentTheme.
	 * @param width
	 *            Largura da legenda gerada.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */

	@SuppressWarnings("unchecked")
	public native void drawLegends(Vector themesLegends,
			Vector<String> themeTitle, int width, int height, boolean fixed,
			boolean columns, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite alterar a cor de fundo padrï¿½o da ï¿½rea de desenho, canvas. A cor
	 * de fundo padrï¿½o ï¿½ preto puro, ou seja os valores das componentes
	 * vermelho, verde, azul sï¿½o zero (0,0,0).
	 * 
	 * @param r
	 *            Componente vermelha da cor de preenchimento do canvas, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param g
	 *            Componente verde da cor de preenchimento do canvas, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param b
	 *            Componente azul da cor de preenchimento do canvas, valores
	 *            vï¿½lidos no intervalo (0-255).
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setCanvasBackgroundColor(int r, int g, int b,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite recuperar a escala atual. A unidade de medida ï¿½ o metro, para um
	 * valor de pixel de (0,28 mm X 0,28 mm).
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return pixel Valor do pixel para a configuraï¿½ï¿½o do box da ï¿½rea de
	 *         interesse com relaï¿½ï¿½o ao tamanho da imagem do dispositivo de
	 *         visualizaï¿½ï¿½o, tela, e o valor unitï¿½rio do pixel adotado como um
	 *         valor mï¿½dio padrï¿½o de (0,28 mm X 0,28 mm).
	 */
	public native double getScale(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Habilita/Desabilita a detecï¿½ï¿½o de conflito ao imprimir rï¿½tulos de texto
	 * sobre o desenho de mapa.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param conflictDetect
	 *            Verdadeiro habilita a detecï¿½ï¿½o de conflito e falso desabilita.
	 * 
	 *            <pre>
	 * true = Habilita.
	 * false = Desabilita.
	 * 
	 *            <pre>
	 * @param sessionId Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor de aplicaï¿½ï¿½o no
	 * momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio, quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um identificador ï¿½nico.
	 * 
	 */
	public native void setConflictDetect(boolean conflictDetect,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Define a coluna da tabela de atributo que classificarï¿½, por padrï¿½o, em
	 * ordem decrescente de prioridade a impressï¿½o de rï¿½tulos de texto sobre o
	 * desenho de mapa. TODO: Permitir a alteraï¿½ï¿½o da ordenaï¿½ï¿½o dos rï¿½tulos pelo
	 * atributo de prioridade.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param fieldName
	 *            Nome da coluna da tabela de atributos usada na ordenaï¿½ï¿½o.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setPriorityField(String fieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define a tolerï¿½ncia para o calculo de colisï¿½o entre rï¿½tulos de texto
	 * sobre o desenho de mapa.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param numPixels
	 *            Valor da tolerï¿½ncia, em pixels, para o calculo de colisï¿½o de
	 *            textos.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setMinCollisionTolerance(int numPixels, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define o tamanho mï¿½nimo que uma geometria deve ter quando apresentada na
	 * ï¿½rea de desenho do dispositivo de saï¿½da, tela, para que esta receba um
	 * rï¿½tulo dinï¿½mico.
	 * 
	 * @see <a
	 *      href="#getImageMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getImageMap</a>
	 * @param n
	 *            Valor da ï¿½rea, em pixels, para o filtro de geometrias que
	 *            assumem um tamanho reduzido na imagem de saï¿½da.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setGeneralizedPixels(int n, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define a coluna da tabela de atributo que terï¿½ seus registros usados para
	 * a impressï¿½o de rï¿½tulos de texto sobre o desenho de mapa.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param fieldName
	 *            Nome da coluna da tabela de atributos usada como fonte dos
	 *            textos a serem desenhados.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setLabelField(String fieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define se o texto de rï¿½tulo estï¿½tico desenhado serï¿½ desenhado com borda.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param turnon
	 *            Desenha com borda ou sem borda.
	 * 
	 *            <pre>
	 * true = com borda.
	 * false = sem borda.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setTextOutLineEnable(boolean turnon, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define a cor da borda do texto de rï¿½tulo estï¿½tico.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param r
	 *            Componente vermelha da cor de borda do texto, valores vï¿½lidos
	 *            no intervalo (0-255).
	 * @param g
	 *            Componente verde da cor de borda do texto, valores vï¿½lidos no
	 *            intervalo (0-255).
	 * @param b
	 *            Componente azul da cor de borda do texto, valores vï¿½lidos no
	 *            intervalo (0-255).
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setTextOutLineColor(int r, int g, int b, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define os valores das propriedades name e id, que se deseja incluir na
	 * tag do elemento HTML <b>map</b>, como no exemplo. <br/>
	 * &gt;map name="map" id="map"&lt;
	 * 
	 * @see <a
	 *      href="#getImageMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getImageMap</a>
	 * @param mapName
	 *            Valor da propriedade <b>name</b> da tag do elemento HTML
	 *            <b>map</b>.
	 * @param mapId
	 *            Valor da propriedade <b>id</b> da tag do elemento HTML
	 *            <b>map</b>.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setImageMapProperties(String mapName, String mapId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Define se a tag do elemento HTML <b>map</b>, serï¿½ fechada. Uma situaï¿½ï¿½o
	 * para o nï¿½o fechamento seria incluir mais elementos <b>area</b> no corpo
	 * da tag antes de fechar a tag. <br/>
	 * &gt;map name="map" id="map"&lt;...&gt;/map&lt;
	 * 
	 * @see <a
	 *      href="#getImageMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getImageMap</a>
	 * @param hasToClose
	 *            Verdadeiro,fecha a tag, falso nï¿½o fecha.
	 * 
	 *            <pre>
	 * true = fecha.
	 * false = nï¿½o fecha.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void closeImageMap(boolean hasToClose, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define o nome do atributo para a tag do elemento HTML <b>area</b>,
	 * incluindo um texto usado como valor fixo do atributo definido seguido de
	 * um caracter curinga, %s, que permite compor o valor fixo do atributo com
	 * um valor vindo de uma coluna da tabela de atributos.
	 * 
	 * <pre>
	 * &gt;area title="ï¿½rea:1234 mï¿½"&lt;
	 * &gt;area title="ï¿½rea:5432 mï¿½"&lt;
	 * &gt;area title="ï¿½rea:987 mï¿½"&lt;
	 * Para gerar esta saï¿½da, use os seguintes valores:
	 * setAreaProperty("title", "ï¿½rea:%s mï¿½", "coluna_area_lote", "12345")
	 * </pre>
	 * 
	 * @see <a
	 *      href="#getImageMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getImageMap</a>
	 * @param propertyName
	 *            Nome do atributo desejado para a tag <b>area</b>.
	 * @param propertyValue
	 *            Valor alfanumperico fixo para compor o valor do atributo.
	 * @param valueSrc
	 *            Nome da coluna da tabela de atributos que servirï¿½ de fonte
	 *            para compor o valor do atributo.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 */
	public native void setAreaProperty(String propertyName,
			String propertyValue, String valueSrc, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite gerar um mapa da imagem, conforme a definiï¿½ï¿½o de mapa de imagem
	 * para Web em HTML, usando a tag <b>map</b>. ï¿½ possï¿½vel usar atributos de
	 * outras tabelas que nï¿½o a de atributos estï¿½tica, ver definiï¿½ï¿½o de tabelas
	 * estï¿½ticas para layers TerraLib, para filtrar a saï¿½da recuperando apenas
	 * os rï¿½tulos das geometrias que satisfaï¿½am uma condiï¿½ï¿½o.
	 * 
	 * @see <a
	 *      href="#setAreaProperty(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">setAreaProperty</a>
	 * @see <a
	 *      href="#closeImageMap(boolean, java.lang.String)">closeImageMap</a>
	 * @see <a
	 *      href="#setImageMapProperties(java.lang.String, java.lang.String, java.lang.String)">setImageMapProperties</a>
	 * @see <a
	 *      href="#setGeneralizedPixels(int, java.lang.String)">setGeneralizedPixels</a>
	 * @param from
	 *            tabela de atributos ou subquery para ser usada como clausula
	 *            from.
	 * @param linkAttr
	 *            nome da coluna da tabela ou subquery fornecida na clausula
	 *            from, que permita identificar as geometrias da camada que se
	 *            deseja incluir rï¿½tulos dinï¿½micos.
	 * @param restrictionExpression
	 *            Clausula de filtro para restringir a saï¿½da.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Uma String contendo um cï¿½digo HTML, construido com base na tag
	 *         <b>map</b> e sua subtag <b>area</b>.
	 */
	public native String getImageMap(String from, String linkAttr,
			String restrictionExpression, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Este mï¿½todo permite recuperar um endereï¿½o ou uma lista de endereï¿½os que
	 * satisfaï¿½am os filtros fornecidos. Ao finalizar o processamento de busca,
	 * um valor numï¿½rico indicando o estado final da busca ï¿½ gerado para
	 * informar o que foi achado e quais as possibilidades usadas durante a
	 * busca. O funcionamento deste mï¿½todo pressupï¿½e que a camada geogrï¿½fica de
	 * informaï¿½ï¿½es sobre vias tenha sido preparada para responder a este tipo de
	 * pesquisa.
	 * 
	 * @see <a
	 *      href="#prepareGeocodingEnvironment(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)">prepareGeocodingEnvironment</a>
	 * @param locationName
	 *            Nome simples ou parte do nome simples da via de interesse,
	 *            este parï¿½metro ï¿½ obrigatï¿½rio.
	 * @param locationNumber
	 *            Nï¿½mero do endereï¿½o a ser localizado, este parï¿½metro ï¿½
	 *            obrigatï¿½rio.
	 * @param neighborhood
	 *            Nome do bairro, nï¿½o obrigatï¿½rio.
	 * @param zipCode
	 *            Nï¿½mero do CEP, nï¿½o obrigatï¿½rio.
	 * @param locationType
	 *            Classificaï¿½ï¿½o de tipo da via, nï¿½o obrigatï¿½rio.
	 * @param locationTitle
	 *            Tï¿½tulo da via, nï¿½o obrigatï¿½rio.
	 * @param locationPreposition
	 *            Preposiï¿½ï¿½o usada para formar o nome completo da via, nï¿½o
	 *            obrigatï¿½rio.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return O retorno deste mï¿½todo ï¿½ um Vector onde:
	 * 
	 *         <pre>
	 * [0] ï¿½ um inteiro que representa o status da operaï¿½ï¿½o realizada pela TerraLib;
	 * <b>Valores possï¿½veis para o estado sï¿½o:</b>
	 * 
	 * 0     -> Problema com a conexï¿½o ao banco (ATENï¿½ï¿½O: Este evento ï¿½ lanï¿½ado como Exception)
	 * 1     -> Endereï¿½o nï¿½o encontrado.
	 * 2     -> Endereï¿½o vazio. (ATENï¿½ï¿½O: Este evento ï¿½ lanï¿½ado como Exception)
	 * 3     -> Erro na SQL de busca. (ATENï¿½ï¿½O: Este evento ï¿½ lanï¿½ado como Exception)
	 * 4     -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome e nï¿½mero.
	 * 5     -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome e nï¿½mero.
	 * 6     -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome sem o nï¿½mero.
	 * 7     -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome sem o nï¿½mero.
	 * 8     -> Endereï¿½o encontrado por similaridade com o nome.
	 * 9     -> Endereï¿½o encontrado usando o bairro ou o CEP.
	 * 10    -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome e nï¿½mero sem tipo.
	 * 11    -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome e nï¿½mero sem o tipo.
	 * 12    -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome sem o nï¿½mero e sem o tipo.
	 * 13    -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome sem o nï¿½mero e sem o tipo.
	 * 14    -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome e nï¿½mero sem o tipo e titulo.
	 * 15    -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome e nï¿½mero sem o tipo e titulo.
	 * 16    -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome sem o nï¿½mero, sem o tipo e sem o titulo.
	 * 17    -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome sem o nï¿½mero, sem o tipo e sem o titulo.
	 * 18    -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome e nï¿½mero sem o tipo, sem o titulo e sem a preposiï¿½ï¿½o.
	 * 19    -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome e nï¿½mero sem o tipo, sem o titulo e sem a preposiï¿½ï¿½o.
	 * 20    -> Endereï¿½o ï¿½nico encontrado atravï¿½s do nome sem o nï¿½mero, sem o tipo, sem o titulo e sem a preposiï¿½ï¿½o.
	 * 21    -> Vï¿½rios endereï¿½os encontrados atravï¿½s do nome sem o nï¿½mero, sem o tipo, sem o titulo e sem a preposiï¿½ï¿½o.
	 * 
	 * [n] demais indices sï¿½o vetores, tipo Vector onde cada vetor ï¿½ o conjunto de dados relacionados aos endereï¿½os
	 * localizados a partir das informaï¿½ï¿½es de entrada fornecidas. Sï¿½o eles:
	 * <b>Valores das variï¿½veis de cada posiï¿½ï¿½o do array de descriï¿½ï¿½o de um endereï¿½o:</b>
	 * 
	 * [0]  Tipo (java.lang.String). Valor do identificador ï¿½nico da geometria que representa o trecho da via onde o ponto foi encontrado.
	 * (Atenï¿½ï¿½o: Apï¿½s este processamento, como o ponto ainda nï¿½o foi localizado, este valor nï¿½o ï¿½ preenchido).
	 * [1]  Tipo (java.lang.Integer). Nï¿½mero inicial esquerdo.
	 * [2]  Tipo (java.lang.Integer). Nï¿½mero final esquerdo.
	 * [3]  Tipo (java.lang.Integer). Nï¿½mero inicial direito.
	 * [4]  Tipo (java.lang.Integer). Nï¿½mero final esquerdo.
	 * [5]  Tipo (java.lang.String). A classificaï¿½ï¿½o do tipo da via (Av., Rua, R., Pr., ...)
	 * [6]  Tipo (java.lang.String). O tï¿½tulo da via (Dr., Dra., Pref., ...)
	 * [7]  Tipo (java.lang.String). A preposiï¿½ï¿½o usada para formar o nome da via (Dos, Das, De, ...)
	 * [8]  Tipo (java.lang.String). O nome da via.
	 * [9]  Tipo (java.lang.String). O nome completo da via.
	 * [10] Tipo (java.lang.String). O nome do bairro do lado esquerdo da via.
	 * [11] Tipo (java.lang.String). O nome do bairro do lado direito da via.  
	 * [12] Tipo (java.lang.String). O CEP do lado esquerdo da via.
	 * [13] Tipo (java.lang.String). O CEP do lado direito da via.
	 * [14] Tipo (java.awt.geom.Point2D.Double) A coordenada do ponto que representa o endereï¿½o pesquisado.
	 * (Atenï¿½ï¿½o: Apï¿½s este processamento o ponto ainda nï¿½o foi localizado, apenas os dados do endereï¿½o).
	 * [15] Tipo (java.lang.Boolean). Indica se o ponto localizado ï¿½ uma coordenada vï¿½lida.
	 * (Atenï¿½ï¿½o: Apï¿½s este processamento, como o ponto ainda nï¿½o foi localizado, este valor nï¿½o ï¿½ vï¿½lido).
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native Vector getAddressesDescription(String locationName,
			int locationNumber, String neighborhood, String zipCode,
			String locationType, String locationTitle,
			String locationPreposition, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Mï¿½todo nativo que permite recuperar o ponto associado a um endereï¿½o,
	 * definido pelo conjunto de informaï¿½ï¿½es do endereï¿½o, encontrado pelo mï¿½todo
	 * de busca de endereï¿½os, getAddressesDescription, e representado no
	 * parï¿½metro addressDescription. Este mï¿½todo utiliza um algoritmo de
	 * interpolaï¿½ï¿½o capaz de identificar um ponto correspondente a um endereï¿½o,
	 * mesmo que o nï¿½mero fornecido nï¿½o exista, devendo apenas pertencer a um
	 * dos intervalos numï¿½ricos, lado esquerdo ou direito da via, de algum dos
	 * trechos pertencentes ï¿½ uma via identificada pelo nome.
	 * 
	 * @see <a
	 *      href="#getAddressesDescription(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getAddressesDescription</a>
	 * @param locationNumber
	 *            Nï¿½mero a ser localizado na via identificada pelos dados
	 *            fornecidos pelo parï¿½metro addressDescription.
	 * @param addressDescription
	 *            Um vetor com os dados que permitem identificar de forma ï¿½nica
	 *            uma via.
	 * 
	 *            <pre>
	 * <b>Valores das variï¿½veis de cada posiï¿½ï¿½o do array de descriï¿½ï¿½o de um endereï¿½o:</b>
	 * 
	 * [0]  Tipo (java.lang.String). Valor do identificador ï¿½nico da geometria que representa o trecho da via onde o ponto foi encontrado.
	 * (Atenï¿½ï¿½o: Este valor serï¿½ preenchido, caso o ponto seja localizado).
	 * [1]  Tipo (java.lang.Integer). Nï¿½mero inicial esquerdo.
	 * [2]  Tipo (java.lang.Integer). Nï¿½mero final esquerdo.
	 * [3]  Tipo (java.lang.Integer). Nï¿½mero inicial direito.
	 * [4]  Tipo (java.lang.Integer). Nï¿½mero final esquerdo.
	 * [5]  Tipo (java.lang.String). A classificjava.lang.Stringo tipo da via (Av., Rua, R., Pr., ...)
	 * [6]  Tipo (java.lang.String). O tï¿½tulo da via (Dr., Dra., Pref., ...)
	 * [7]  Tipo (java.lang.String). A preposiï¿½ï¿½o usada para formar o nome da via (Dos, Das, De, ...)
	 * [8]  Tipo (java.lang.String). O nome da via.
	 * [9]  Tipo (java.lang.String). O nome completo da via.
	 * [10] Tipo (java.lang.String). O nome do bairro do lado esquerdo da via.
	 * [11] Tipo (java.lang.String). O nome do bairro do lado direito da via.  
	 * [12] Tipo (java.lang.String). O CEP do lado esquerdo da via.
	 * [13] Tipo (java.lang.String). O CEP do lado direito da via.
	 * [14] Tipo (java.awt.geom.Point2D.Double) A coordenada do ponto que representa o endereï¿½o pesquisado.
	 * (Atenï¿½ï¿½o: Este valor serï¿½ preenchido, caso o ponto seja localizado).
	 * [15] Tipo (java.lang.Boolean). Indica se o ponto localizado ï¿½ uma coordenada vï¿½lida.
	 * (Atenï¿½ï¿½o: Este valor serï¿½ preenchido, caso o ponto seja localizado).
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * 
	 * @return O retorno deste mï¿½todo ï¿½ um Vector, com a mesma estrutura do
	 *         parï¿½metro addressDescription, contendo os valores devidamente
	 *         preenchidos para os itens:
	 * 
	 *         <pre>
	 * [0]  Tipo (java.lang.String). Valor do identificador ï¿½nico da geometria que representa o trecho da via onde o ponto foi encontrado.
	 * [14] Tipo (java.awt.geom.Point2D.Double) A coordenada do ponto que representa o endereï¿½o pesquisado.
	 * (Atenï¿½ï¿½o: Este valor serï¿½ preenchido, caso o ponto seja localizado).
	 * [15] Tipo (java.lang.Boolean). Indica se o ponto localizado ï¿½ uma coordenada vï¿½lida.
	 * (Atenï¿½ï¿½o: Este valor serï¿½ preenchido, caso o ponto seja localizado).
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native Vector getPointCoordinate(int locationNumber,
			Vector<Object> addressDescription, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Desenha o ponto encontrado pelo mï¿½todo getPointCoordinate na ï¿½rea de
	 * desenho, canvas, considerando que a coordenada do ponto estï¿½ na projeï¿½ï¿½o
	 * da vista corrente.
	 * 
	 * @param aPoint
	 *            O ponto a ser desenhado.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel desenhar o ponto e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean drawPointAddress(Point2D aPoint, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Acessa os metadados das tabelas de atributos relacionadas com uma camada
	 * de dados geogrï¿½ficos, Layer, referï¿½nciado pelo tema corrente ou de
	 * referï¿½ncia. Ex: (Static attribute table name)-(Geo attribute
	 * link),(column1 name):(column1 type);(column2 name):(column2
	 * type)#(External attribute table name)-(External table attribute
	 * link)-(Static table attribute link),(column1 name):(column1 type)
	 * 
	 * @param themeType
	 *            Tipo de definiï¿½ï¿½o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de referï¿½ncia.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Lista dos metadados, em formato padronizado como:
	 * 
	 *         <pre>
	 * (Static attribute table name)-(Geo attribute link),(column1 name):(column1 type);(column2 name):(column2 type)#(External attribute table name)-(External table attribute link)-(Static table attribute link),(column1 name):(column1 type);(column2 name):(column2 type)
	 * </pre>
	 */
	public native String getThemeMetadata(int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Destroe a instï¿½ncia do objeto nativo, TerraJava, associado a uma sessï¿½o.
	 * Limpa a memï¿½ria.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel destruir o objeto associado a
	 *         uma sessï¿½o e falso (false) caso contrï¿½rio.
	 */
	public native boolean destroySession(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite definir o nï¿½mero mï¿½ximo de instï¿½ncias ativas do objeto nativo,
	 * TerraJava. 
	 * 
	 * @param maxInstances
	 *            Nï¿½mero mï¿½ximo de conexï¿½es.
	 */
	public native void setMaxInstances(int maxInstances)
			throws IllegalAccessException, InstantiationException;


	/**
	 * Permite gerar um mapa temï¿½tico, usando um atributo alfanumï¿½rico para
	 * agrupar as geometrias, filtrando ou nï¿½o a saï¿½da por um atributo
	 * alfanumï¿½rico igual ou diferente do usado para agrupar geometrias. Os
	 * objeto geogrï¿½ficos sï¿½o representados por um tema, configurado como
	 * corrente. Adicionalmente, ï¿½ gerada a legenda do agrupamento solicitado e
	 * retornada em formato de um vetor a ser passado numa lista junto com os
	 * outros temas desenhados para o mï¿½todo drawLegends()
	 * 
	 * @param jfields
	 *            Nome da coluna usada para gerar o agrupamento
	 * @param jfrom
	 *            A tabela usada como tabela de atributos, a partir da qual foi
	 *            especificada a coluna no parï¿½metro jfields.
	 * @param jlinkAttr
	 *            Nome da coluna que permite ligar os atributos com os objetos
	 *            geogrï¿½ficos referï¿½nciados pelo tema corrente.
	 * @param jwhere
	 *            Clausula de filtro.
	 * @param jnumSlices
	 *            O nï¿½mero de faixas para gerar os grupos de objetos
	 *            geogrï¿½ficos.
	 * @param jgroupType
	 *            O tipo de algoritmo de classificaï¿½ï¿½o usado para agrupar os
	 *            objetos geogrï¿½ficos.
	 * 
	 *            <pre>
	 * 0 = Passos Iguais
	 * 1 = Quantil
	 * 2 = Desvio Padrï¿½o
	 * 3 = Valor ï¿½nico
	 * 
	 * <pre>
	 * @param r Incluir ou nï¿½o a componente vermelha na legenda das classes geradas
	 * 
	 *            <pre>
	 * true = inclui
	 * false = nï¿½o inclui
	 * </pre>
	 * @param g
	 *            Incluir ou nï¿½o a componente verde na legenda das classes
	 *            geradas
	 * 
	 *            <pre>
	 * true = inclui
	 * false = nï¿½o inclui
	 * </pre>
	 * @param b
	 *            Incluir ou nï¿½o a componente azul na legenda das classes
	 *            geradas
	 * 
	 *            <pre>
	 * true = inclui
	 * false = nï¿½o inclui
	 * </pre>
	 * @param jprec
	 *            Nï¿½mero de casas decimais consideradas usada na apresentaï¿½ï¿½o
	 *            dos intervalos de cada faixa gerada.
	 * @param jstdDev
	 *            O coeficiente de variaï¿½ï¿½o usado para permitir a comparaï¿½ï¿½o
	 *            entre as faixas geradas quando o algoritmo de agrupamento
	 *            escolhido ï¿½ o desvio padrï¿½o.
	 * @param jsessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Um vetor de legendas que deverï¿½ ser usado para o desenho das
	 *         legendas no mï¿½todo drawLegend
	 */
	@SuppressWarnings("unchecked")
	public native Vector drawGroupSql(int jtypeField, String jfields,
			String jfrom, String jlinkAttr, String jwhere, int jnumSlices,
			int jgroupType, boolean r, boolean g, boolean b, int jprec,
			int jstdDev, String jsessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Cria as tabelas de coleï¿½ï¿½o para um tema corrente. Ver modelo de dados
	 * TerraLib. Gera o ponto de referï¿½ncia onde um texto de rï¿½tulo para uma
	 * geometria pode ser desenhado. Caso um identificador de um objeto
	 * geogrï¿½fico seja fornecido, realiza a operaï¿½ï¿½o apenas para o objeto
	 * geogrï¿½fico identificado.
	 * 
	 * @param objectId
	 *            Identificador de um objeto geogrï¿½fico.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar esta operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean buildCollection(String objectId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	public native boolean isValidBox(double xmin, double ymin, double xmax,
			double ymax, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite importar para o banco de dados um arquivo de dados vetoriais no
	 * formato de transporte ShapeFile (ESRI).
	 * 
	 * @param units
	 *            Unidade de medida para a projeï¿½ï¿½o do dado.
	 * @param lat0
	 *            Latitude para a projeï¿½ï¿½o do dado.
	 * @param lon0
	 *            Longitude para a projeï¿½ï¿½o do dado.
	 * @param stlat1
	 *            Paralelo padrï¿½o 1 para a projeï¿½ï¿½o do dado.
	 * @param stlat2
	 *            Paralelo padrï¿½o 2 para a projeï¿½ï¿½o do dado.
	 * @param scale
	 *            Escala para a projeï¿½ï¿½o do dado.
	 * @param joffx
	 *            Offset X para a projeï¿½ï¿½o do dado.
	 * @param offy
	 *            Offset Y para a projeï¿½ï¿½o do dado.
	 * @param hemisphereNorth
	 *            O Hemisfï¿½rio norte para a projeï¿½ï¿½o do dado.
	 * @param projectionName
	 *            O nome para a projeï¿½ï¿½o do dado.
	 * @param datum
	 *            A elipsoide para a projeï¿½ï¿½o do dado.
	 * @param filePath
	 *            Caminho completo e nome do arquivo onde estï¿½o os daods
	 *            vetoriais.
	 * @param layerName
	 *            O nome da camada, layer, a ser criado no banco para
	 *            referï¿½nciar os dados importados.
	 * @param linkName
	 *            O nome da coluna de ligaï¿½ï¿½o entre a tabela de atributos e a
	 *            tabela de geometrias que serï¿½o criadas para guardar os dados.
	 * @param attrTableName
	 *            O nome da tabela de atributos que serï¿½ criada.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a importaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean importShape(String filePath, String layerName,
			HashMap projectionMap, String linkName, String attrTableName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Retorna os nomes das coluna disponï¿½veis na tabela de atributo do arquivo
	 * shape.
	 * 
	 * @param filePath
	 *            Caminho completo e nome do arquivo onde estï¿½o os dados
	 *            vetoriais.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Um vetor tipo Vector, com os nomes das colunas da tabela de
	 *         atributos do arquivo dbf, que compï¿½e o formato ShapeFile.
	 */
	@SuppressWarnings("unchecked")
	public Vector loadAttrTableMetadataFromShape(String filePath,
			String sessionId) throws IllegalAccessException,
			InstantiationException {
		return loadAttrTableMetadataFromShape(filePath, new Vector(), sessionId);
	}

	@SuppressWarnings("unchecked")
	private native Vector loadAttrTableMetadataFromShape(String filePath,
			Vector vectorObj, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Lï¿½ configuraï¿½ï¿½es de projeï¿½ï¿½o do shape.
	 * 
	 * @param filePath
	 *            Caminho completo e nome do arquivo onde estï¿½o os dados
	 *            vetoriais.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Um vetor tipo Vector, com os dados sobre a projeï¿½ï¿½o do dado.
	 * 
	 *         <pre>
	 * <b>Ordem do retorno no vetor:</b>
	 * 
	 * [0]  String units Unidade de medida para a projeï¿½ï¿½o do dado.
	 * [1]  Double lat0 Latitude para a projeï¿½ï¿½o do dado.
	 * [2]  Double lon0 Longitude para a projeï¿½ï¿½o do dado.
	 * [3]  Double stlat1 Paralelo padrï¿½o 1 para a projeï¿½ï¿½o do dado.
	 * [4]  Double stlat2 Paralelo padrï¿½o 2 para a projeï¿½ï¿½o do dado.
	 * [5]  Double scale Escala para a projeï¿½ï¿½o do dado.
	 * [6]  Double offx Offset X para a projeï¿½ï¿½o do dado.
	 * [7]  Double offy Offset Y para a projeï¿½ï¿½o do dado.
	 * [8]  Boolean hemisphereNorth O Hemisfï¿½rio norte para a projeï¿½ï¿½o do dado.
	 * [9]  String projectionName O nome para a projeï¿½ï¿½o do dado.
	 * [10] String datum A elipsoide para a projeï¿½ï¿½o do dado.
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native HashMap loadProjectionFromShape(String filePath,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Exporta para arquivo tipo ShapeFile a partir do theme corrente. Ainda ï¿½
	 * possï¿½vel passar um vetor com os nomes das coluna que deseja que sejam
	 * exportadas para o arquivo dbf. A operaï¿½ï¿½o de exportaï¿½ï¿½o leva em conta o
	 * box da ï¿½rea de interesse, e exporta apenas as geometrias que interceptam
	 * o box visivel.
	 * 
	 * @param filePath
	 *            Caminho completo onde serï¿½o gravados os arquivos ShapeFile,
	 *            exportados.
	 * @param attrVec
	 *            Vetor com os nomes das colunas da tabela de atributos da
	 *            camada de dados geogrï¿½ficos que serï¿½ exportada. Caso a lista
	 *            esteja vazia serï¿½o exportadas todas as colunas.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a exportaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public native boolean saveThemeToFile(String filePath, Vector attrVec,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite importar as geometrias de um determinado layer existente em um
	 * arquivo dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de destino.
	 * @param layerName
	 *            Nome do Layer de destino. O layer que serï¿½ criado no banco.
	 * @param geomType
	 *            Tipo de geometria a ser importado, dentre os existentes para o
	 *            layer selecionado.
	 * 
	 *            <pre>
	 * 0 = Todas as representaï¿½ï¿½es
	 * 1 = Poligonos
	 * 2 = Linhas
	 * 4 = Pontos
	 * 128 = texto
	 * </pre>
	 * @param strDxfLayer
	 *            Um nome de layer dentre os disponï¿½veis no arquivo dxf.
	 * @param units
	 *            Unidade de medida para a projeï¿½ï¿½o do dado.
	 * @param lat0
	 *            Latitude para a projeï¿½ï¿½o do dado.
	 * @param lon0
	 *            Longitude para a projeï¿½ï¿½o do dado.
	 * @param stlat1
	 *            Paralelo padrï¿½o 1 para a projeï¿½ï¿½o do dado.
	 * @param stlat2
	 *            Paralelo padrï¿½o 2 para a projeï¿½ï¿½o do dado.
	 * @param scale
	 *            Escala para a projeï¿½ï¿½o do dado.
	 * @param offx
	 *            Offset X para a projeï¿½ï¿½o do dado.
	 * @param offy
	 *            Offset Y para a projeï¿½ï¿½o do dado.
	 * @param hemisphereNorth
	 *            O Hemisfï¿½rio norte para a projeï¿½ï¿½o do dado.
	 * @param projectionName
	 *            O nome para a projeï¿½ï¿½o do dado.
	 * @param datum
	 *            A elipsoide para a projeï¿½ï¿½o do dado.
	 * @param attrList
	 *            Lista de colunas para compor a tabela de atributos.
	 * 
	 *            <pre>
	 * <b>Lista de colunas</b>
	 * [n] = Vector<Object>
	 * 
	 * <b>Descriï¿½ï¿½o de coluna</b>
	 * [0] = String type
	 * [1] = Integer length
	 * [2] = String columnName
	 * [3] = Boolean isPrimaryKey
	 * </pre>
	 * @param linkName
	 *            Nome da coluna usada para associar uma geometria a seu
	 *            atributo.
	 * @param attrTableName
	 *            Nome da tabela de atributos a ser criada.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a exportaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean importDxf(String filePath, String layerName,
			int geomType, String strDxfLayer, String units, double lat0,
			double lon0, double stlat1, double stlat2, double scale,
			double offx, double offy, boolean hemisphereNorth,
			String projectionName, String datum, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite exportar as geometrias de um plano existente no banco de dados
	 * para um arquivo dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de destino.
	 * @param layerName
	 *            Nome de um layer existente no banco de dados.
	 * @param geomType
	 *            Tipo de geometria a ser exportada.
	 * 
	 *            <pre>
	 * 0 = Todas as representaï¿½ï¿½es
	 * 1 = Poligonos
	 * 2 = Linhas
	 * 4 = Pontos
	 * 128 = texto
	 * </pre>
	 * @param whereClause
	 *            Filtro para permitir a exportaï¿½ï¿½o de geometrias especï¿½ficas.
	 *            Usar o id de identificaï¿½ï¿½o de uma geometria.
	 * 
	 *            <pre>
	 * Exemplo: object_id in (1230, 2345, 4387)
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a exportaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean exportDxf(String filePath, String layerName,
			int geomType, String whereClause, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Acesso aos tipos de geometrias existentes em um layer especï¿½fico,
	 * existente em um dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de origem.
	 * @param layerName
	 *            Nome do layer selecionado.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Tipo de geometrias disponï¿½veis em um layer.
	 * 
	 *         <pre>
	 * 1 = Poligono
	 * 2 = Linha
	 * 4 = Ponto
	 * 128 = Texto
	 * 256 = Cï¿½lula
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native Vector dxfGeometryTypeFromLayer(String filePath,
			String layerName, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Acesso ao nï¿½mero de layers existentes em um arquivo dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de origem.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Nï¿½mero de layers encontrados no arquivo dxf.
	 */
	public native Integer dxfLayersCount(String filePath, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Acesso ï¿½ lista de layers existentes em um arquivo dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de origem.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Um vetor que representa a lista de nomes dos layer disponï¿½veis no
	 *         arquivo dxf.
	 */
	@SuppressWarnings("unchecked")
	public native Vector dxfListLayers(String filePath, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Recupera a lista de nomes das camadas de dados, layers, disponï¿½veis na
	 * base.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Um vetor que representa a lista de nomes dos layers disponï¿½veis
	 *         na base de dados.
	 */
	@SuppressWarnings("unchecked")
	public Vector getLayersName(String sessionId)
			throws IllegalAccessException, InstantiationException {
		return getLayersName(new Vector(), sessionId);
	}

	@SuppressWarnings("unchecked")
	private native Vector getLayersName(Vector vectorObj, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo permite configurar os parï¿½metros para operaï¿½ï¿½o de agrupamento
	 * personalizado. Criar as faixas desejadas, com uma cor de legenda para
	 * cada faixa, o intervalo de valores para cada faixa, uma descriï¿½ï¿½o para
	 * cada faixa. A quantidade de objetos geomï¿½tricos que estï¿½o em cada faixa ï¿½
	 * apenas para compor a informaï¿½ï¿½o da legenda de cada faixa, sendo opcional.
	 * 
	 * @see <a
	 *      href="#drawGroupSqlAndLegend(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, boolean, boolean, boolean, int, int, int, java.lang.String)">drawGroupSqlAndLegend</a>
	 * @param legendTitle
	 *            Titulo da legenda.
	 * @param redList
	 *            Listas de cores, componete vermelha.
	 * @param greenList
	 *            Listas de cores, componete verde.
	 * @param blueList
	 *            Listas de cores, componete azul.
	 * @param maxList
	 *            Lista de valores mï¿½ximos de cada faixa.
	 * @param minList
	 *            Lista de valores minimos de cada faixa.
	 * @param descList
	 *            Lista de descriï¿½ao de cada faixa.
	 * @param numObjList
	 *            Lista de quantidade de objetos de cada faixa.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */
	@SuppressWarnings("unchecked")
	public native void setCustomGroupParameters(String legendTitle,
			Vector redList, Vector greenList, Vector blueList, Vector minList,
			Vector maxList, Vector descList, Vector numObjList, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo permite recuperar os parï¿½metros usados em uma agrupamento
	 * anterior utilizando uma algoritmo prï¿½-formatado (Passos Iguais, Valor
	 * ï¿½nico , Desvio Padrï¿½o..) para realizar um agrupamento personalizado
	 * usando as faixas geradas.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Lista de faixas utilizadas no ultimos agrupamento realizado, no
	 *         seguinte formato:
	 * 
	 *         <pre>
	 * 		Vector<HashMap> faixas 
	 * 		HashMap faixa = faixas.get(pos);
	 * 		String from = faixa.get("from"); 
	 * 		String to = faixa.get("to");
	 * 		int count = faixa.get("count");
	 * 		String description = faixa.get("description");
	 * 		HashMap color = faixa.get("color");
	 * 		int r = color.get("r");
	 * 		int g = color.get("g");
	 * 		int b = color.get("b");
	 * </pre>
	 * 
	 *         from -> Limite Inicial da Faixa to -> Limite Final da Faixa
	 *         description -> Descriï¿½ï¿½o da faixa color -> Cor da Faixa em RGB
	 * 
	 */
	@SuppressWarnings("unchecked")
	public native Vector getCustomGroupParameters(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo cria um novo layer com o nome, projeï¿½ï¿½o, lista de atributos e
	 * box informados.
	 * 
	 * @param layerName
	 *            Nome do layer a ser criado
	 * @param projectionHashMap
	 *            Projeï¿½ï¿½o do novo layer
	 * @param attList
	 *            Lista de atributos Para o parï¿½metro attList considerar que ï¿½
	 *            um vector onde cada indice possui outro vector com os
	 *            metadados descritivos de cada coluna da tabela de atributos
	 *            obedecendo a seguinte convensï¿½o: 0 => string (descriï¿½ï¿½o do
	 *            data type) (TeSTRING, TeREAL, etc...) 1 => int (comprimento do
	 *            campo) 2 => string (nome do campo) 3 => int (se campo ï¿½ chave
	 *            primï¿½ria)
	 * @param x1
	 *            X da coordenada inferior esquerda do Box
	 * @param y1
	 *            Y da coordenada inferior esquerda do Box
	 * @param x2
	 *            X da coordenada superior direita do Box
	 * @param y2
	 *            Y da coordenada superior direita do Box
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean createLayer(String layerName,
			HashMap<String, Object> projectionHashMap, Vector attList,
			double x1, double y1, double x2, double y2, Vector geomRepVec, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo cria uma vista com o nome e username requisitado.
	 * 
	 * @param viewName
	 *            Nome da vista a ser criada
	 * @param userName
	 *            Nome do usuï¿½rio a ser associado como dono da nova vista.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean createView(String viewName, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Mï¿½todo para atualizaï¿½ï¿½o de um layer.
	 * 
	 * @param layerId
	 *            id do layer que serï¿½ atualizado.
	 * @param newLayerName
	 *            Novo nome do layer.
	 * @param projectionHashMap
	 *            Nova Projeï¿½ï¿½o do layer
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean updateLayer(int layerId, String newLayerName,
			HashMap<String, Object> projectionHashMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo renomeia a vista corrente com o novo nome solicitado.
	 * 
	 * @param viewNewName
	 *            Novo nome da vista.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean updateView(String viewNewName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo cria um tema a partir de uma camada de dados, layer, com o
	 * nome especificado. O tema associa um estilo de visualizaï¿½ï¿½o aos dados
	 * brutos da camada de dados, layer. Na criaï¿½ï¿½o um tema recebe um visual
	 * padrï¿½o para todas as geometrias que este mapeia. ï¿½ possï¿½vel denifir a
	 * qual tema grupo o novo tema serï¿½ associado como filho.
	 * 
	 * @param themeName
	 *            Nome do theme a ser criado
	 * @param layerName
	 *            Nome do layer a partir do qual o tema serï¿½ criado.
	 * @param parentId
	 *            Id do tema grupo pai na qual o tema serï¿½ associado como filho.
	 *            Caso nï¿½o associar a nenhum grupo, passar o valor como 0.
	 * @param restriction
	 *            Clausula SQL que serï¿½ usada para restringir com os atributos
	 *            os objetos geogrï¿½ficos do layer no novo tema criado.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public boolean createTheme(String themeName, String layerName,
			String sessionId) throws IllegalAccessException,
			InstantiationException {
		return createTheme(themeName, layerName, 0, "", sessionId);
	}

	public native boolean createTheme(String themeName, String layerName,
			int parentId, String restriction, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo modifica o theme corrente da vista corrente. Atualiza o nome
	 * do tema e o id do tema grupo parente
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param themeNewName
	 *            Nome do tema a ser alterado, tema alvo.
	 * @param parentId
	 *            Id do tema grupo parente a qual este tema deve ser adicionado
	 *            como filho, se 0 ou o seu mesmo id ele ficarï¿½ na raiz da
	 *            hierarquia.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean updateTheme(String themeNewName, int parentId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	public boolean updateTheme(String themeNewName, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return updateTheme(themeNewName, 0, sessionId);
	}

	/**
	 * Esse mï¿½todo renomeia a vista corrente.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @param viewNewName
	 *            Novo nome da vista.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean updateViewName(String viewNewName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo remove um layer do banco de dados, dado um id.
	 * 
	 * @param layerId
	 *            id do layer que serï¿½ removido.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean deleteLayer(int layerId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo remove a vista corrente.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean removeView(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo remove o tema corrente.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean removeTheme(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo retorna os metadados (Tabelas de atributos, atributos,
	 * representaï¿½ï¿½es e projeï¿½ï¿½o) dos layers disponï¿½veis no database conectado
	 * 
	 * @param forceReload
	 *            Forï¿½ar recarregamento dos layers do banco de dados
	 * @param loadAttrList
	 *            Carregar lista de tabelas de atributos ou nï¿½o
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public native Vector getLayerSet(boolean forceRealod, boolean loadAttrList,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Esse mï¿½todo retorna os metadados (Tabelas de atributos, atributos) do
	 * layer requisitado.
	 * 
	 * @param layerId
	 *            Identificador do layer para carregar os atributos
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public native Vector getLayerAttrTables(int layerId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Este mï¿½todo retorna o metadado das views, temas, layers e atributos do
	 * banco de dados.
	 * 
	 * @param dbUsername
	 *            Nome de um usuï¿½rio do banco de dados. Lembre-se, as vistas sï¿½o
	 *            associadas aos usuï¿½rios.
	 * @param forceReload
	 *            Forï¿½a re-leitura dos metadados do banco. Valor padrï¿½o = false.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna todos os metadados das vistas, temas, layers e atributos
	 *         em um vetor tipo Vector&gt;HashMap&lt;
	 */
	@SuppressWarnings("unchecked")
	public native Vector getViewSet(String dbUsername, boolean forceReload,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Este mï¿½todo retorna o metadado das views, temas, layers e atributos do
	 * banco de dados respeitando a forma de hierarquia da arvore de temas e
	 * temas de groupo.
	 * 
	 * @param forceReload
	 *            Forï¿½a re-leitura dos metadados do banco. Valor padrï¿½o = false.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna todos os metadados das vistas, temas, layers e atributos
	 *         em um vetor tipo Vector&gt;HashMap&lt;
	 */
	@SuppressWarnings("unchecked")
	public native Vector getViewSetTree(boolean forceReload, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Este mï¿½todo retorna os metadados configurados para permitir as operaï¿½ï¿½es
	 * de geocodificaï¿½ï¿½o para um layer.
	 * 
	 * @param layerId
	 *            Id do layer requisitado.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Hashmap O mapa descrito abaixo.
	 * 
	 *         <pre>
	 * <b>Um Hashmap de retorno apresenta as seguintes chaves com seus respectivos valores:</b>
	 * locationCompleteName Nome da coluna da tabela de atributos que armazena o nome completo da via.
	 * tableId Identificador da tabela de atributos usada na preparaï¿½ï¿½o para o processamento de operaï¿½ï¿½es de geocodificaï¿½ï¿½o.
	 * initialLeftNumber Nome da coluna da tabela de atributos que armazena o nï¿½mero inicial esquerdo da via.
	 * initialRightNumber Nome da coluna da tabela de atributos que armazena o nï¿½mero inicial direito da via.
	 * finalLeftNumber Nome da coluna da tabela de atributos que armazena o nï¿½mero final esquerdo da via.
	 * finalRightNumber Nome da coluna da tabela de atributos que armazena o nï¿½mero final direito da via.
	 * locationType Nome da coluna da tabela de atributos que armazena o tipo da via.
	 * locationTitle Nome da coluna da tabela de atributos que armazena o titulo da via.
	 * locationPreposition Nome da coluna da tabela de atributos que armazena a preposiï¿½ï¿½o usada para formar o nome da via.
	 * locationName Nome da coluna da tabela de atributos que armazena o nome simple da via.
	 * leftNeighborhood Nome da coluna da tabela de atributos que armazena o bairro do lado esquerdo da via.
	 * rightNeighborhood Nome da coluna da tabela de atributos que armazena o bairro do lado direito da via.
	 * leftZipCode Nome da coluna da tabela de atributos que armazena o CEP do lado esquerdo da via.
	 * rightZipCode Nome da coluna da tabela de atributos que armazena o CEP do lado direito da via.
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getGeocodingEnvironment(int layerId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo prepara o ambiente de geocodificaï¿½ï¿½o passando os parï¿½metros
	 * necessï¿½rio a serem cadastrados para realizar uma busca de endereï¿½o,
	 * geocodificaï¿½ï¿½o.
	 * 
	 * @param layerId
	 *            Identificador do layer
	 * @param initialLeftNumber
	 *            Nome da coluna da tabela de atributos que armazena o nï¿½mero
	 *            esquerdo inicial
	 * @param initialRightNumber
	 *            Nome da coluna da tabela de atributos que armazena o nï¿½mero
	 *            direito inicial
	 * @param finalLeftNumber
	 *            Nome da coluna da tabela de atributos que armazena o nï¿½mero
	 *            esquerdo final
	 * @param finalRightNumber
	 *            Nome da coluna da tabela de atributos que armazena o nï¿½mero
	 *            direito final
	 * @param locationType
	 *            Nome da coluna da tabela de atributos que armazena o tipo da
	 *            via.
	 * @param locationTitle
	 *            Nome da coluna da tabela de atributos que armazena o titulo da
	 *            via.
	 * @param locationPreposition
	 *            Nome da coluna da tabela de atributos que armazena a
	 *            preposiï¿½ï¿½o usada para compor o nome completo da via.
	 * @param locationName
	 *            Nome da coluna da tabela de atributos que armazena o nome
	 *            simples da via.
	 * @param leftNeighborhood
	 *            Nome da coluna da tabela de atributos que armazena o nome do
	 *            bairro do lado esquerdo da via.
	 * @param rightNeighborhood
	 *            Nome da coluna da tabela de atributos que armazena o nome do
	 *            bairro do lado direito da via.
	 * @param leftZipCode
	 *            Nome da coluna da tabela de atributos que armazena o nï¿½mero do
	 *            CEP do lado esquerdo da via.
	 * @param rightZipCode
	 *            Nome da coluna da tabela de atributos que armazena o nï¿½mero do
	 *            CEP do lado direito da via.
	 * @param locationCompleteName
	 *            Nome da coluna da tabela de atributos que armazena o nome
	 *            completo da via, caso exista.
	 * @param nameColumnCompleteName
	 *            Nome da coluna a ser criada para o nome completo da via, caso
	 *            nï¿½o exista ou queira criar uma coluna nova. Esta operaï¿½ï¿½o
	 *            executa um update na tabela de atributos, e pode ser demorada
	 *            dependendo do nï¿½mero de registros existente nesta tabela.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean prepareGeocodingEnvironment(int layerId,
			String initialLeftNumber, String initialRightNumber,
			String finalLeftNumber, String finalRightNumber,
			String locationType, String locationTitle,
			String locationPreposition, String locationName,
			String leftNeighborhood, String rightNeighborhood,
			String leftZipCode, String rightZipCode,
			String locationCompleteName, String nameColumnCompleteName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Esse mï¿½todo permite definir se na prï¿½xima requisiï¿½ï¿½o de desenho a
	 * representaï¿½ï¿½o de texto serï¿½ desenhada. Este texto nï¿½o deve ser confundido
	 * com o os rï¿½tulos estï¿½ticos ou dinï¿½micos. Trata-se dos textos definidos
	 * como geometrias pelo TerraView.
	 * 
	 * @param drawText
	 *            Verdadeiro ou falso.
	 * 
	 *            <pre>
	 * true = desenha
	 * false = nï¿½o desenha.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */
	public native void setDrawTextRepresentation(boolean drawText,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Esse mï¿½todo permite a configuraï¿½ï¿½o, persistida ou nï¿½o dos limites
	 * inferior e superior de escala de visualizaï¿½ï¿½o.
	 * 
	 * @param minScale
	 *            Limite inferior de escala
	 * @param maxScale
	 *            Limite superior de escala
	 * @param persistScaleLimit
	 *            Verdadeiro ou falso.
	 * 
	 *            <pre>
	 * true = persiste
	 * false = nï¿½o persiste.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean setThemeScaleLimit(double minScale, double maxScale,
			boolean persistScaleLimit, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo permite a recuperaï¿½ï¿½o dos limites superior e inferior de
	 * escala de visualizaï¿½ï¿½o definidida para o tema corrente.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return HashMap com as keys (maxScale, minScale)
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getThemeScaleLimit(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo permite inserï¿½ï¿½o de geometrias em um layer. Configurar a
	 * vista corrente e um tema, ï¿½ prï¿½-requisito.
	 * 
	 * @param representation
	 *            Tipo de representaï¿½ï¿½o vetorial:
	 * 
	 *            <pre>
	 * 1 = poligono
	 * 2 = linha
	 * 4 = ponto
	 * </pre>
	 * @param verticeList
	 *            Lista de vï¿½rtices.
	 * 
	 *            <pre>
	 * <b>Estrutura dos hash maps:</b>
	 * 
	 * Coordenadas:
	 * 		Vector<Object> coordsList = new Vector<Object>(); 
	 * 		HashMap<String,Double> coordMap = new HashMap<String,Double>();
	 * 		coordMap.put("x",-45.94422044878433);
	 * 		coordMap.put("y",-23.10596463174134);
	 * 		coordsList.add(coordMap);
	 * </pre>
	 * 
	 * @param attrList
	 *            Lista de atributos para a geometria.
	 * 
	 *            <pre>
	 * <b>Deve apresentar o seguinte formato:</b>
	 * 
	 * Vector<Object> attributesVector = new Vector<Object>();	
	 * HashMap attributeMap = new HashMap();
	 * 	attributeMap.put("key", attribute.getFieldName());
	 * attributeMap.put("value", attribute.getFieldValue());
	 * attributesVector.add(attributeMap);
	 * 
	 * HashMap attributeMap2 = new HashMap();
	 * 	attributeMap2.put("key", attribute.getFieldName());
	 * attributeMap2.put("value", attribute.getFieldValue());
	 * attributesVector.add(attributeMap2);
	 * </pre>
	 * @param projectionHashMap
	 *            Parï¿½metros da projeï¿½ï¿½o em um HashMap.
	 * 
	 *            <pre>
	 * <b>Lista padronizada de parï¿½metros (exemplo de uso):</b>
	 * 
	 * HashMap<String, Object> projectionMap = new HashMap<String, Object>();
	 * projectionMap.put("projDatum", projection.getDatum());
	 * projectionMap.put("projName", projection.getName());
	 * projectionMap.put("projLat0", projection.getLat0());
	 * projectionMap.put("projLon0", projection.getLon0());
	 * projectionMap.put("projStLat1", projection.getStlat1());
	 * projectionMap.put("projStLat2", projection.getStlat2());
	 * projectionMap.put("projScale", projection.getScale());
	 * projectionMap.put("projOffx", projection.getOffx());
	 * projectionMap.put("projOffy", projection.getOffy());
	 * projectionMap.put("projNorthHemisphere", projection.getHemNorth());
	 * </pre>
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean addGeometry(int representation,
			Vector<Object> verticeList, Vector<Object> attrList,
			String layerName, HashMap<String, Object> projectionHashMap,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Este mï¿½todo permite e reprojeï¿½ï¿½o de coordenadas geogrï¿½ficas da projeï¿½ï¿½o
	 * do dado para uma projeï¿½ï¿½o especifica.
	 * 
	 * @param coordsList
	 *            Lista de hash maps com as coordenadas X,Y
	 * 
	 *            <pre>
	 * <b>Estrutura do hashmap para coordenadas:</b>
	 * 
	 * Vector<Object> coordsList = new Vector<Object>(); 
	 * HashMap<String,Double> coordMap = new HashMap<String,Double>();
	 * coordMap.put("x",-45.94422044878433);
	 * coordMap.put("y",-23.10596463174134);
	 * coordsList.add(coordMap);
	 * </pre>
	 * @param dataProjectionMap
	 *            Hash map com os parï¿½metros de projeï¿½ï¿½o atual das coordenadas
	 * @param destinationProjectionMap
	 *            Hash map com os parametros de projeï¿½ï¿½o desejada
	 * 
	 *            <pre>
	 * <b>Estrutura do hashmap para projeï¿½ï¿½es:</b>
	 * 
	 * HashMap<String, Object> projectionMap = new HashMap<String, Object>();
	 * projectionMap.put("projDatum", projection.getDatum());
	 * projectionMap.put("projName", projection.getName());
	 * projectionMap.put("projLat0", projection.getLat0());
	 * projectionMap.put("projLon0", projection.getLon0());
	 * projectionMap.put("projStLat1", projection.getStlat1());
	 * projectionMap.put("projStLat2", projection.getStlat2());
	 * projectionMap.put("projScale", projection.getScale());
	 * projectionMap.put("projOffx", projection.getOffx());
	 * projectionMap.put("projOffy", projection.getOffy());
	 * projectionMap.put("projNorthHemisphere", projection.getHemNorth());
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Lista das coordenadas de entrada remapeadas para a projeï¿½ï¿½o de
	 *         saï¿½da especificada pelo parï¿½metro destinationProjectionMap. Segue
	 *         a mesma estrutura do vetor de entrada, definido pelo parï¿½metro
	 *         coordsList.
	 */
	public native Vector<Object> remapCoordinates(Vector<Object> coordsList,
			HashMap<String, Object> dataProjectionMap,
			HashMap<String, Object> destinationProjectionMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo permite a uniï¿½o do box dos temas especificados pela lista de
	 * Ids de temas.
	 * 
	 * @param themesId
	 *            Lista de ids de temas, formato Vector.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna um HashMap com o box resultante.
	 * 
	 *         <pre>
	 * <b>Chaves:</b>
	 * - x1
	 * - y1
	 * - x2
	 * - y2
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getThemesBox(Vector themesId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite definir a atuaï¿½ï¿½o do controle de escala sobre os temas. O
	 * controle de escala permite que as geometrias representadas por um tema
	 * sejam desenhadas em intervalos de escalas prï¿½ definidos, um intervalo por
	 * tema, melhorando o desempenho e a apresentaï¿½ï¿½o de camadas de dados que
	 * possuem nï¿½veis de detalhamento melhor definidos em escalas diferentes.
	 * Atenï¿½ï¿½o: Atua apenas nas camadas para as quais existe definiï¿½ï¿½o de
	 * intervalo de escala.
	 * 
	 * @see <a
	 *      href="#setThemeScaleLimit(double, double, boolean, java.lang.String)">setThemeScaleLimit</a>
	 * @param scaleControlEnabled
	 *            Verdadeiro ou falso.
	 * 
	 *            <pre>
	 * true = liga o controle de escala.
	 * false = desliga o controle de escala.
	 * </pre>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */
	public native void setAutomaticScaleControlEnable(
			boolean scaleControlEnabled, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria uma vista em memï¿½ria a partir de um documento SLD xml.
	 * 
	 * @param path
	 *            O caminho do documento SLD.
	 * @param userName
	 *            Nome do usuï¿½rio a ser associado como dono da nova vista.
	 * @param viewName
	 *            Nome para a vista que serï¿½ criada. Caso vazio, utiliza o nome
	 *            definido no documento SLD.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna os temas invï¿½lidos, definidos em tempo de criaï¿½ï¿½o. O
	 *         hashmap possui (nome,tipo) do tema.
	 */
	public native HashMap<String, Integer> createViewFromSLD(String path,
			String userName, String viewName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Salva a vista corrente para um documento SLD xml;
	 * 
	 * @param path
	 *            Um caminho para o documento SLD.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */
	public native void saveCurrentView(String path, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria uma vista em memï¿½ria com temas previamente armazenados no banco.
	 * 
	 * @param viewName
	 *            Nome para a nova vista.
	 * @param themeIds
	 *            Identificadores dos temas previamente armazenados no banco e
	 *            que serï¿½o incoporados a nova vista.
	 * @param projectionHashMap
	 *            Projeï¿½ï¿½o da nova vista.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true se a vista foi criada com sucesso, falso caso
	 *         contrï¿½rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean createViewMem(String viewName, String user,
			Vector themeIds, HashMap<String, Object> projectionHashMap,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Carrega um tema armazenado no banco de dados para a vista corrente.
	 * 
	 * @param themeId
	 *            Identificador do tema.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */
	public native void loadTheme2View(int themeId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria um tema do tipo arquivo na vista corrente.
	 * 
	 * @param themeName
	 *            Nome para o novo tema.
	 * @param path
	 *            Caminho para o dado do tema. Ex. ../../brasil.shp
	 * @param parentId
	 * @param projectionHashMap
	 *            Projeï¿½ï¿½o do tema.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true se o tema foi criado com sucesso, falso caso
	 *         contrï¿½rio.
	 */
	public native boolean createFileTheme(String themeName, String path,
			int parentId, HashMap<String, Object> projectionHashMap,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Remove um tema da memï¿½ria.
	 * 
	 * @param themeId
	 *            Identificador do tema que serï¿½ removido.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true se o tema foi removido da vista com sucesso, falso
	 *         caso contrï¿½rio.
	 */
	public native boolean removeThemeMem(int themeId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Renomeia em memï¿½ria o tema corrente.
	 * 
	 * @param newName
	 *            Novo nome para o tema corrente
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true se o tema corrente foi renomeado com sucesso, falso
	 *         caso contrï¿½rio.
	 */
	public native boolean renameThemeMem(String newName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse mï¿½todo modifica o file theme corrente da vista corrente. Atualiza o
	 * nome do tema e o id do tema grupo parente
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param newName
	 *            Nome do tema a ser alterado, tema alvo.
	 * @param parentId
	 *            Id do tema grupo parente a qual este tema deve ser adicionado
	 *            como filho, se 0 ou o seu mesmo id ele ficarï¿½ na raiz da
	 *            hierarquia.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Verdadeiro (true) se foi possï¿½vel realizar a operaï¿½ï¿½o e falso
	 *         (false) caso contrï¿½rio.
	 */
	public native boolean updateFileTheme(String newName, int parentId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Define o caminho para o dado de um tema do tipo arquivo.
	 * 
	 * @param themeName
	 *            Nome do tema arquivo que serï¿½ atualizado.
	 * @param path
	 *            Caminho para o dado do tema. Ex. ../../brasil.shp
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */
	public native void setFileThemePath(String themeName, String path,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	public native byte[] drawThemeLegend(String title, int width, int height,
			boolean fixed, boolean columns, int legendImageType,
			boolean legendOpaque, int legendImageQuality, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Salva um vista previamente criada em memï¿½ria para o banco de dados.
	 * 
	 * @param viewName
	 *            Nome da vista que serï¿½ armazenada no banco de dados.
	 * @param userName
	 *            Nome do usuï¿½rio proprietï¿½rio da vista.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true se a vista foi armazenada com sucesso, falso caso
	 *         contrï¿½rio.
	 */
	public native boolean saveView2DB(String viewName, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Remove da memï¿½ria uma ista.
	 * 
	 * @param viewName
	 *            Nome da vista que serï¿½ removida da memï¿½ria.
	 * @param userName
	 *            Nome do usuï¿½rio proprietï¿½rio da vista.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true se a vista foi removida com sucesso, falso caso
	 *         contrï¿½rio.
	 */
	public native boolean removeViewMem(String viewName, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Obtï¿½m o nome da vista definido em um documento SLD xml.
	 * 
	 * @param path
	 *            Caminho para o documento SLD xml;
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna o nome da vista.
	 */
	public native String getViewNameFromSLD(String path, String sessionId)
			throws IllegalAccessException, InstantiationException;

	public native boolean saveFileTheme2DB(String name, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * A funï¿½ï¿½o permite a definiï¿½ï¿½o dos temas visiveis e nï¿½o visiveis para a
	 * vista corrente. A visibilidade dos temas pode ser definida tanto no banco
	 * de dados como somente na memï¿½ria da sessï¿½o atual, passando o parametro
	 * persist true or false.
	 * 
	 * @param themesVec
	 *            Vetor de HashMaps com duas chaves: id e visibilidade do tema O
	 *            Vetor deve conter HashMaps de todos os temas da vista corrente
	 *            com suas respectivas visibilidades.
	 * 
	 *            <pre>
	 * <b>Exemplo de estrutura</b>
	 * Vector<HashMap> themesVect = new Vector<HashMap>()
	 * HashMap themeVisMap1 = new HashMap();
	 * HashMap themeVisMap2 = new HashMap();
	 * themeVisMap1.put("themeId", 12); <-- Tema com id 12
	 * themeVisMap1.put("visibility", true); <-- Tema visivel
	 * themeVisMap2.put("themeId", 13); <-- Tema com id 13
	 * themeVisMap2.put("visibility", false); <-- Tema nï¿½o visivel   
	 * themesVect.add(themesVisMap1);
	 * themesVect.add(themesVisMap2);
	 * </pre>
	 * 
	 * @param persist
	 *            Deseja persistir no banco de dados ou manter simplesmente em
	 *            memï¿½ria
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Sucesso ao gravar visibilidade dos temas ou nï¿½o.
	 */
	@SuppressWarnings("unchecked")
	public native boolean setThemesVisibility(Vector<HashMap> themesVec,
			boolean persist, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Funï¿½ï¿½o permite a criaï¿½ï¿½o de grupo de temas direto na vista ou dentro de
	 * outro grupo de temas. ï¿½ necessï¿½rio setar a vista corrente onde serï¿½
	 * inserido o tema.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @param themeGroupName
	 *            Nome do tema grupo que serï¿½ criado.
	 * @param parentId
	 *            Id do tema grupo parente onde serï¿½ criado o tema. Caso 0 serï¿½
	 *            criado na vista.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return
	 */
	public native boolean createThemeGroup(String themeGroupName, int parentId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Funï¿½ï¿½o permite apagar um tema grupo e todos seus filhos. ï¿½ necessï¿½rio
	 * setar a vista corrente onde serï¿½ inserido o tema.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @param themeGroupId
	 *            Id do tema grupo quer serï¿½ apagado
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return
	 */
	public native boolean deleteThemeGroup(int themeGroupId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Funï¿½ï¿½o permite alterar o nome de um tema grupo e alterar sua posiï¿½ï¿½o na
	 * ï¿½rvore de temas, passando como parametro o id do grupo na qual o tema
	 * pertencerï¿½, caso 0 serï¿½ na vista.
	 * 
	 * @param themeGroupId
	 *            Id do tema grupo quer serï¿½ alterado
	 * @param themeGroupNewName
	 *            Novo nome para o tema
	 * @param parentId
	 *            Id do grupo para onde o tema serï¿½ filho
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return
	 */
	public native boolean updateThemeGroup(int themeGroupId,
			String themeGroupNewName, int parentId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Funï¿½ï¿½o permite salvar a ordem/prioridade no carregamento e no desenho dos
	 * temas e temas grupos.
	 * 
	 * @param themesList
	 *            Lista de Maps de temas com o id do tema e o nï¿½mero da
	 *            prioridade.
	 * 
	 *            <pre>
	 * <b>Exemplo de estrutura</b>
	 * Vector<HashMap> themesList = new Vector<HashMap>();
	 * 
	 *  HashMap themeMap1 = new HashMap();
	 *  themeMap1.put("themeId", 10);
	 *  themeMap1.put("themePriority", 0);
	 *  themesList.add(themeMap1);
	 *  
	 *  HashMap themeMap2 = new HashMap();
	 *  themeMap2.put("themeId", 11);
	 *  themeMap2.put("themePriority", 1);
	 *  themesList.add(themeMap2);
	 *  
	 * @param sessionId Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor de aplicaï¿½ï¿½o no
	 * momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio, quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um identificador ï¿½nico.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public native boolean saveThemesPriorities(Vector themesList,
			boolean persist, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Funï¿½ï¿½o retorna lista de Id's dos temas que podem ser plotados na BBox e
	 * escala atuais.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @see <a
	 *      href="#setWorld(double, double, double, double, int, int, java.lang.String)">setWorld</a>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Lista de id's dos temas visiveis
	 */
	public native Vector<Integer> getThemesToPlot(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Funï¿½ï¿½o permite a criaï¿½ï¿½o de uma tabela temporï¿½ria com uma coluna e com os
	 * valores requisitados. Utilizada quando existem muitos registros para
	 * executar numa SQL inn (na qual possui limite de caracteres), podendo
	 * assim fazer um join com a tabela temporï¿½ria ao invï¿½s de usar a clausula.
	 * 
	 * @param tableName
	 *            Nome da tabela a ser criada
	 * @param columnName
	 *            Nome da coluna a ser criada na tabela temporï¿½ria para os
	 *            valores
	 * @param valuesVector
	 *            Lista de valores para serem adicionados na tabela.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso tenha criado a tabela.
	 */
	public native boolean createTemporaryTableWithValues(String tableName,
			String columnName, Vector<String> valuesVector, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Deleta tabela temporï¿½ria que foi previamente criada.
	 * 
	 * @param tableName
	 *            Nome da tabela existente.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso tenha deletado a tabela.
	 */
	public native boolean deleteTemporaryTable(String tableName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	public native void setWorkProjection(HashMap<String, Object> projection,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Funï¿½ï¿½o permite recuperar todas as geometrias do tema corrente no formato
	 * WKB
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Geometrias no formato WKB
	 */
	public native byte[] getThemeGeometriesOnWKB(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Funï¿½ï¿½o permite recuperar geometrias com os identificadores dos objetos
	 * solicitados do tema corrente no formato WKT
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param oids
	 *            Lista de identificadores dos objetos.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Geometrias no formato WKT
	 */
	public native String getGeometriesByOidOnWKT(Vector<String> oids,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Funï¿½ï¿½o permite recuperar geometrias com os identificadores das geometrias
	 * solicitadas do tema corrente no formato WKT
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param oids
	 *            Lista de identificadores das geometrias.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Geometrias no formato WKT
	 */
	public native String getGeometriesByGeomIdOnWKT(Vector<String> geomIds,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Funï¿½ï¿½o permite recuperar todas as geometrias do tema corrente na seguinte
	 * estrutura: Vector<HashMap> geometries = new Vector<HashMap>(); for(i) {
	 * HashMap geometry = geometries.get(i); Integer type =
	 * geometry.get("type"); Vector<Vector<HashpMap>> linearRingVector =
	 * geometry.get("vertexes"); for(j) { Vector<HashMap> vertexesVector =
	 * linearRingVector.get(j); for(h) { HashMap vertex = vertexesVector.get(h);
	 * Double x = vertex.get("x"); Double i = vertex.get("y"); } }
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Geometrias do tema
	 */
	@SuppressWarnings("unchecked")
	public native Vector getThemeGeometries(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Funï¿½ï¿½o permite recuperar geometrias com os Objects Ids solicitados do
	 * tema corrente na seguinte estrutura: Vector<HashMap> geometries = new
	 * Vector<HashMap>(); for(i) { HashMap geometry = geometries.get(i); Integer
	 * type = geometry.get("type"); Vector<Vector<HashpMap>> linearRingVector =
	 * geometry.get("vertexes"); for(j) { Vector<HashMap> vertexesVector =
	 * linearRingVector.get(j); for(h) { HashMap vertex = vertexesVector.get(h);
	 * Double x = vertex.get("x"); Double i = vertex.get("y"); } }
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Geometrias do tema
	 */
	@SuppressWarnings("unchecked")
	public native Vector getGeometriesByOid(Vector<String> Oids,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Realiza a interseï¿½ï¿½o entre o tema corrente e o tema de referï¿½ncia.
	 * 
	 * @param layerName
	 *            Nome do novo layer que serï¿½ gerado pela operaï¿½ï¿½o.
	 * @param useThemeOverlayAttr
	 *            Define se os atributos do tema de referï¿½ncia devem ser
	 *            incluï¿½dos no novo layer.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso a operaï¿½ï¿½o tenha sido realizada corretamente,
	 *         false caso contrï¿½rio.
	 */
	public native boolean intersection(String layerName,
			boolean useThemeOverlayAttr, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a interseï¿½ï¿½o entre o tema corrente (gerado a partir de uma layer
	 * raster) e o tema de referï¿½ncia.
	 * 
	 * @param layerName
	 *            Nome do novo layer que serï¿½ gerado pela operaï¿½ï¿½o.
	 * @param backValue
	 *            Valor usado para background (dummy)
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso a operaï¿½ï¿½o tenha sido realizada corretamente,
	 *         false caso contrï¿½rio.
	 */
	public native boolean intersectionRaster(String layerName,
			double backValue, String sessiondId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Realiza a diferenï¿½a entre o tema corrente e o tema de referï¿½ncia nesta
	 * ordem: (tema corrente - tema referï¿½ncia).
	 * 
	 * @param layerName
	 *            Nome do novo layer que serï¿½ gerado pela operaï¿½ï¿½o.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso a operaï¿½ï¿½o tenha sido realizada corretamente,
	 *         false caso contrï¿½rio.
	 */
	public native boolean difference(String layerName, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a interseï¿½ï¿½o entre o tema corrente e uma lista de geometrias em
	 * memï¿½ria. A lista em memï¿½ria ï¿½ preenchida utilizando o mï¿½todo
	 * locateObject, definindo o parï¿½metro stroreGeom = true
	 * 
	 * @param layerName
	 *            Nome do novo layer que serï¿½ gerado pela operaï¿½ï¿½o.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso a operaï¿½ï¿½o tenha sido realizada corretamente,
	 *         false caso contrï¿½rio.
	 */
	public native boolean mask(String layerName, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a interseï¿½ï¿½o entre o tema corrente (gerado a partir de uma layer
	 * raster) e uma lista de geometrias em memï¿½ria. A lista em memï¿½ria ï¿½
	 * preenchida utilizando o mï¿½todo locateObject, definindo o parï¿½metro
	 * stroreGeom = true
	 * 
	 * @param layerName
	 *            Nome do novo layer que serï¿½ gerado pela operaï¿½ï¿½o.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso a operaï¿½ï¿½o tenha sido realizada corretamente,
	 *         false caso contrï¿½rio.
	 */
	public native boolean maskRaster(String layerName, double backValue,
			String sessiondId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Realiza a diferenï¿½a entre o tema corrente e uma lista de geometrias em
	 * memï¿½ria. A lista em memï¿½ria ï¿½ preenchida utilizando o mï¿½todo
	 * locateObject, definindo o parï¿½metro stroreGeom = true
	 * 
	 * @param layerName
	 *            Nome do novo layer que serï¿½ gerado pela operaï¿½ï¿½o.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso a operaï¿½ï¿½o tenha sido realizada corretamente,
	 *         false caso contrï¿½rio.
	 */
	public native boolean differenceM(String layerName, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Limpa a lista de geometrias em memï¿½ria.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 */
	public native void clearGeomList(String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a agregaï¿½ï¿½o no tema corrente a partir da lista de atributos
	 * selecionados.
	 * 
	 * @param layerName
	 *            Nome do novo layer que serï¿½ gerado pela operaï¿½ï¿½o.
	 * @param agregAttrs
	 *            Atributos que serï¿½o a base da agregaï¿½ï¿½o.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna true caso a operaï¿½ï¿½o tenha sido realizada corretamente,
	 *         false caso contrï¿½rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean aggregation(String layerName, Vector agregAttrs,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	@SuppressWarnings("unchecked")
	public native boolean add(String layerName, Vector themeIds,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	@SuppressWarnings("unchecked")
	public native boolean addGeometries(Vector<HashMap> wktgeoms,
			HashMap projectionMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	@SuppressWarnings("unchecked")
	public native boolean updateGeometries(Vector<HashMap> oids,
			HashMap projectionMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	public native boolean deleteGeometries(Vector<String> geomIds,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	@SuppressWarnings("unchecked")
	public native boolean addObjects(Vector<HashMap> geoObjects,
			HashMap dataProjectionMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	@SuppressWarnings("unchecked")
	public native boolean updateObjects(Vector<HashMap> geoObjects,
			HashMap dataProjectionMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	public native boolean associateGeometryToObject(int geomId,
			String objectId, String sessionId) throws IllegalAccessException,
			InstantiationException;

	public native boolean importKml(String filePath, String layerName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite a associaï¿½ï¿½o de informaï¿½ï¿½es (nome, autor, fonte, qualidade,
	 * descriï¿½ï¿½o, data de criaï¿½ï¿½o, hora de criaï¿½ï¿½o) a um layer jï¿½ existente na
	 * base de dados.
	 * 
	 * @param layerId
	 *            Idenficador do layer criado previamente
	 * @param name
	 *            Nome do layer (Apelido)
	 * @param author
	 *            Autor do layer
	 * @param source
	 *            Fonte da informaï¿½ï¿½o do layer
	 * @param quality
	 *            Qualidade do layer
	 * @param description
	 *            Descriï¿½ï¿½o
	 * @param date
	 *            Data de criaï¿½ï¿½o
	 * @param hour
	 *            Hora de criaï¿½ï¿½o
	 * @param transf
	 *            ?
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorn true caso sucesso na associaï¿½ï¿½o.
	 */
	public native boolean setLayerMetadata(int layerId, String name,
			String author, String source, String quality, String description,
			String date, String hour, boolean transf, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a recuperaï¿½ï¿½o de informaï¿½ï¿½es associadas a um layer existente na
	 * base de dados.
	 * 
	 * @param layerId
	 *            Idenficador do layer criado previamente
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return HashMap com as seguintes keys: layerId; name; author; source;
	 *         quality; description; date; hour; tranf;
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getLayerMetadata(int layerId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a recuperaï¿½ï¿½o de informaï¿½ï¿½es associadas a todos os layers
	 * existentes na base de dados.
	 * 
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Vector com HashMaps com as seguintes keys: layerId; name; author;
	 *         source; quality; description; date; hour; tranf;
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getLayersMetadata(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite e exclusï¿½o de todos os objetos (geometrias e attributos) do layer
	 * solicitado mantendo as outras configuraï¿½ï¿½es do layer.
	 * 
	 * @param layerId
	 *            Identificador do layer a ser excluidas as geometrias.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Retorna True caso tenha sucesso na remoï¿½ï¿½o.
	 */
	public native boolean deleteAllObjectsFromLayer(int layerId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite usando como referï¿½ncia um BOX definido previamente setar uma
	 * escala de visualizaï¿½ï¿½o.
	 * 
	 * @param scale
	 *            Escala a setar
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return HashMap com o novo BOX chaves(x1,y1,x2,y2)
	 */
	@SuppressWarnings("unchecked")
	public native HashMap setScale(double scale, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a importaï¿½ï¿½o de imagens rasters dentro de uma mesmo layer
	 * (Mosaico)
	 * 
	 * @param layerName
	 *            Nome do Layer que serï¿½ criado
	 * @param filePathList
	 *            Vetor de String com os caminhos dos rasters
	 * @param multiRes
	 *            Inteiro para a opï¿½ï¿½o de multiresoluï¿½ï¿½o. Valores menores ou
	 *            igual a um definem que nï¿½o haverï¿½ multiresoluï¿½ï¿½o, e valores
	 *            acima definem que haverï¿½, e quantas resoluï¿½ï¿½es serï¿½o.
	 * @param dummy
	 *            , cor de pixel a ser ignorada durante o desenho da imagem
	 *            (0-255)
	 * @param projectionMap
	 *            , projeï¿½ï¿½o para criar o layer, caso a projeï¿½ï¿½o do raster seja
	 *            diferente, o raster serï¿½ reprojetado. Chaves do HashMap:
	 *            projDatum (String), projUnits (String), projName (String),
	 *            projLat0 (Double), projLon0 (Double), projStLat1 (Double),
	 *            projStLat2 (Double), projScale (Double), projOffx (Double),
	 *            projOffy (Double), projNorthHemisphere (Boolean)
	 * @param logPath
	 *            Local onde os logs serï¿½o armazenados.
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return boolean Resultado da operaï¿½ï¿½o
	 */
	@SuppressWarnings("unchecked")
	public native boolean importRasterList(String layerName,
			Vector<String> filePathList, int multiRes, int dummy,
			HashMap projectionMap, String logPath, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Recupera as projeï¿½ï¿½es de uma lista de rasters
	 * 
	 * @param filePathList
	 *            Vetor de String com os caminhos dos rasters
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Lista de HashMaps de projeï¿½ï¿½es - Chaves do HashMap: projDatum (String), projUnits
	 *         (String), projName (String), projLat0 (Double), projLon0
	 *         (Double), projStLat1 (Double), projStLat2 (Double), projScale
	 *         (Double), projOffx (Double), projOffy (Double),
	 *         projNorthHemisphere (Boolean)
	 */
	@SuppressWarnings("unchecked")
	public native Vector<HashMap> getRasterListProjections(Vector<String> filePathList,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Cria um campo relacionado a ï¿½rea na tabela do layer caso o mesmo nï¿½o
	 * exista. Calcula os valores das ï¿½reas das geometrias pertencentes a este
	 * layer.
	 * 
	 * @param layerId
	 *            Idenficador do layer que serï¿½ usado para a operaï¿½ï¿½o
	 * @param areaFieldName
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return boolean Resultado da operaï¿½ï¿½o
	 */
	public native boolean createOrReplaceAreaField(int layerId,
			String areaFieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria um campo relacionado a perï¿½metro na tabela do layer caso o mesmo nï¿½o
	 * exista. Calcula os valores dos perï¿½metros das geometrias pertencentes a
	 * este layer.
	 * 
	 * @param layerId
	 *            Idenficador do layer que serï¿½ usado para a operaï¿½ï¿½o
	 * @param perimeterFieldName
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return boolean Resultado da operaï¿½ï¿½o
	 */
	public native boolean createOrReplacePerimeterField(int layerId,
			String perimeterFieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria 2 campos relacionados a centrï¿½ide na tabela do layer caso o mesmo
	 * nï¿½o exista. Calcula os valores das centrï¿½ides das geometrias pertencentes
	 * a este layer.
	 * 
	 * @param layerId
	 *            Idenficador do layer que serï¿½ usado para a operaï¿½ï¿½o
	 * @param centroidXFieldName
	 * @param centroidYFieldName
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return boolean Resultado da operaï¿½ï¿½o
	 */
	public native boolean createOrReplaceCentroidField(int layerId,
			String centroidXFieldName, String centroidYFieldName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite a criaï¿½ï¿½o de uma nova coluna na tabela de atributos do layer
	 * solicitado com os parametros requisitados.
	 * 
	 * @param layerId
	 *            Idenficador do layer que serï¿½ usado para a operaï¿½ï¿½o
	 * @param columnName
	 *            Nome da coluna a ser criada
	 * @param columnType
	 *            Tipo da coluna a set criada (TeSTRING, TeREAL, TeINT,
	 *            TeDATETIME, TeBLOB, TeOBJECT, TeCHARACTER, TeUNKNOWN,
	 *            TeUNSIGNEDINT, TePOINTTYPE, TeLINE2DTYPE, TePOLYGONTYPE,
	 *            TeCELLTYPE, TeTEXTTYPE, TeNODETYPE, TePOINTSETTYPE,
	 *            TeLINESETTYPE, TePOLYGONSETTYPE, TeCELLSETTYPE, TeTEXTSETTYPE,
	 *            TeNODESETTYPE, TeRASTERTYPE, TeBOOLEAN)
	 * @param columnSize
	 *            Tamanho da coluna a ser criada
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Resultado da operaï¿½ï¿½o
	 */
	public native boolean createAttributeColumn(int layerId, String columnName,
			String columnType, int columnSize, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a exclusï¿½o de uma coluna na tabela de atributos.
	 * 
	 * @param layerId
	 *            Idenficador do layer que serï¿½ usado para a operaï¿½ï¿½o
	 * @param columnName
	 *            Nome da coluna a ser excluï¿½da
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Resultado da operaï¿½ï¿½o
	 */
	public native boolean deleteAttributeColumn(int layerId, String columnName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite a atualizaï¿½ï¿½o de uma coluna na tabela de atributos com os
	 * parametros requisitados. Para PostgreSQL se o parametro oldColumnName ï¿½
	 * passado apenas o nome da coluna ï¿½ atualizada, caso esse parametro seja
	 * passado vazio somente as modificaï¿½ï¿½es do tipo e do tamanho serï¿½o
	 * aplicados na coluna.
	 * 
	 * @param layerId
	 *            Idenficador do layer que serï¿½ usado para a operaï¿½ï¿½o
	 * @param oldColumnName
	 *            Nome atual da coluna para ser alterada
	 * @param newColumnName
	 *            Novo nome para a coluna a ser alterada
	 * @param newColumnType
	 *            Novo tipo para coluna a ser alterada (TeSTRING, TeREAL, TeINT,
	 *            TeDATETIME, TeBLOB, TeOBJECT, TeCHARACTER, TeUNKNOWN,
	 *            TeUNSIGNEDINT, TePOINTTYPE, TeLINE2DTYPE, TePOLYGONTYPE,
	 *            TeCELLTYPE, TeTEXTTYPE, TeNODETYPE, TePOINTSETTYPE,
	 *            TeLINESETTYPE, TePOLYGONSETTYPE, TeCELLSETTYPE, TeTEXTSETTYPE,
	 *            TeNODESETTYPE, TeRASTERTYPE, TeBOOLEAN)
	 * @param newColumnSize
	 *            Tamanho da coluna a ser criada
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Resultado da operaï¿½ï¿½o
	 */
	public native boolean updateAttributeColumn(int layerId,
			String oldColumnName, String newColumnName, String newColumnType,
			int newColumSize, String sessionId) throws IllegalAccessException,
			InstantiationException;
	/**
	 * Variavel que deve ser definida logo apï¿½s a inicializaï¿½ï¿½o da Classe TerraJSP para definir se ao iniciar
	 * as instï¿½ncias e conexï¿½es das sessï¿½es serï¿½ usado o Pool de Conexï¿½es. Para utilizar ï¿½ necessï¿½rio definir
	 * no mesmo momento o nï¿½mero mï¿½ximo de conexï¿½es para o Pool usando a funï¿½ï¿½o setMaxPoolConnections().
	 * @param useConnectionPool Booleana se usa ou nï¿½o o Pool
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native void useConnectionPool(boolean useConnectionPool) throws IllegalAccessException,
			InstantiationException;
	/**
	 * Nï¿½mero mï¿½ximo de conexï¿½es que devem existir no Pool. ï¿½ necessï¿½rio executar essa funï¿½ï¿½o 
	 * logo apï¿½s a instï¿½ncia da classe TerraJSP.
	 * @param maxPoolConnections
	 */
	public native void setMaxPoolConnections(int maxPoolConnections);
	/**
	 * Seta o tempo limite de espera da sessï¿½o que necessitar de uma conexï¿½o ï¿½ nï¿½o existir disponï¿½vel
	 * no Pool.Esse parametro nï¿½o ï¿½ obrigatï¿½rio. Caso necessï¿½rio executar essa funï¿½ï¿½o 
	 * logo apï¿½s a instï¿½ncia da classe TerraJSP.
	 * @param maxPoolWait Tempo de timeout em Milisegundos. 
	 */
	public native void setMaxPoolWait(int maxPoolWait);
	/**
	 * Seta o nï¿½mero mï¿½ximo de conexï¿½es ociosas que ficaram no servidor. Serï¿½o criadas inicialmente
	 * esse nï¿½mero de conexï¿½es. 
	 * @param maxPoolIdle Nï¿½mero mï¿½ximo de conexï¿½es ociosas
	 */
	public native void setMaxPoolIdle(int maxPoolIdle);
	
	public native int getRasterLayerLevels(int layerId, String sessionId) throws IllegalAccessException,
	InstantiationException;
	
	public native boolean loadNetwork(int layerId, String sessionId) throws IllegalAccessException,
	InstantiationException;
	/** 
	 * Mï¿½todo permitir o desenho de uma lista de temas (id ou nome) sem a necessidade de passar o identificador 
	 * do recurso que serï¿½ utilizado no TerraJava/TerraManager. Isso torna a funï¿½ï¿½o stand alone, sem a necessidade
	 * da configuraï¿½ï¿½o previa do ambiente de desenho (setCurrentView(), setWorld(), setTheme(), drawCurrentTheme()).
	 * Para a execuï¿½ï¿½o desse mï¿½todo primeiramente ï¿½ necessï¿½rio executar o mï¿½todo connect(). Esse mï¿½todo foi criado
	 * no intuito de executar requisiï¿½ï¿½es simultï¿½neas de desenho (Multithread).
	 * @param themesList Lista de temas (id ou nome) para ser desenhado no canvas na ordem definida nesse array com o estilo definido
	 * no mapa de estilos. Estrutura do objeto:
	 * Vector<HashMap> themesList
	 * 	|_ HashMap<String, Object> themeMap
	 * 		|_ "themeName", String themeName (opcional ou themeName ou themeId)
	 * 		|_ "themeId", Integer themeId (opcional ou themeId ou themeName)
	 * 		|_ "themeVisualList", Vector<HashMap> themeVisualList 
	 * 			|_HashMap<String, Integer> visualMap
	 * 				|_"geomRep", Integer geomRep
	 * 				|_"colorRed", Integer colorRed   
	 * 				|_"colorGreen", Integer colorGreen
	 * 				|_"colorBlue", Integer colorBlue
	 * 				|_"styleId", Integer styleId
	 * 				|_"transparency", Integer transparency
	 * 				|_"contourColorRed", Integer contourColorRed (SOMENTE PARA POLYGONS)
	 * 				|_"contourColorGreen", Integer contourColorGreen (SOMENTE PARA POLYGONS)
	 * 				|_"contourColorBlue", Integer contourColorBlue (SOMENTE PARA POLYGONS)
	 * 	 			|_"contourStyleId", Integer contourStyleId	 (SOMENTE PARA POLYGONS)
	 * 				|_"contourTransparency", Integer contourTransparency	 (SOMENTE PARA POLYGONS)
	 * 				|_"width", Integer width (SOMENTE PARA POLYGONS e LINES)
	 * 				|_"size", Integer size (SOMENTE PARA POINTS)
	 * 		|_"thematicMap", boolean isThematicMap
	 * 		|_"themeGroupingMap", HashMap<String, Object> themeGrouping
	 * 			|_"groupingType", Integer groupingType
	 * 			|_"groupingAttributeType", Integer groupingAttributeType
	 * 			|_"fields", String fields
	 * 			|_"fromClause", String fromClause
	 * 			|_"linkAttr", String linkAttr
	 * 			|_"restrictionExpression", String restrictionExpression
	 * 			|_"precision", Integer precision
	 * 			|_"stdDev", Integer stdDev (SOMENTE PARA GROUPINGTYPE == 2)
	 * 			|_"numSlices", Integer numSlices (SOMENTE PARA GROUPINGTYPE != 5 e != 3)
	 * 			|_"rampColorsMap", HashMap<String, Boolean> rampColorsMap (SOMENTE PARA GROUPINGTYPE != 5)
	 * 				|_"colorRed", Boolean colorRed   
	 * 				|_"colorGreen", Boolean colorGreen
	 * 				|_"colorBlue", Boolean colorBlue
	 *	 		|_"slicesList", Vector<HashMap> slicesList (SOMENTE PARA GROUPINGTYPE == 5)
	 * 				|_HashMap<String, Object> sliceMap 
	 * 					|_"from", String from
	 * 					|_"to", String to
	 * 					|_"count", Integer count
	 * 					|_"description", String description
	 * 					|_"sliceColorMap", HashMap<String, Integer>
	 * 						|_HashMap<String, Integer> sliceColorMap 
	 * 						|_"colorRed", Integer colorRed   
	 * 						|_"colorGreen", Integer colorGreen
	 * 						|_"colorBlue", Integer colorBlue
	 * 		|_"useLabelConfig", boolean useLabelConfig
	 * 		|_"labelConfigMap", HashMap<String, Object> themeGrouping (OPCIONAL)
	 * 			|_"field", String labelFieldName
	 * 			|_"detectConflict", boolean detectConflict
	 * 			|_"priorityField", String priorityLabelField
	 * 			|_"urbanMode", boolean urbanMode 
	 * 			|_"descTextPriorityOrder", boolean isDescTextPriorityOrder
	 * 			|_"minCollisionTol", Integer minCollisionTol
	 * 			|_"textBox", HashMap<String,Double> textBoxMap
	 * 				|_"x1", Double x1
	 * 				|_"y1", Double y1
	 * 				|_"x2", Double x2
	 * 				|_"y2", Double y2
	 * 			|_"visualMap", HashMap<String, Integer> visualMap
	 * 				|_"colorRed", Integer textColorRed   
	 * 				|_"colorGreen", Integer textColorRed
	 * 				|_"colorBlue", Integer textColorRed
	 * 				|_"fontFamily", String textColorRed
	 * 				|_"width", Integer fontSize
	 * 
	 * Na qual em visualMap: 	
	 * 	<b>	PARA POLYGONS </b>  
	 *  - <b>geomRep</b> Representaï¿½ï¿½o de Polygons valor = 1
	 *  - <b>colorRed</b> Componente vermelha da cor de preenchimento da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>colorGreen</b> Componente verde da cor de preenchimento da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>colorBlue</b> Componente azul da cor de preenchimento da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>styleId</b> Estilo do preenchimento da cï¿½lula ou poligono:
	 *  	; 0 = transparente
	 *  	; 1 = preenchimento opaco
	 *  	; 2 = hachura horizontal
	 *  	; 3 = hachura vertical
	 *  	; 4 = hachura diagonal inclinaï¿½ï¿½o em 135ï¿½
	 *  	; 5 = hachura diagonal inclinaï¿½ï¿½o em 45ï¿½
	 *  	; 6 = hachura horizontal e vertical
	 *  	; 7 = hachura horizontal e vertical inclinada em 45ï¿½
	 * - <b>transparency</b> Cor de preenchimento aceita valores no intervalo (0 - 100), medida de porcentagem, para aplicar nï¿½vel de transparï¿½ncia.
	 * - <b>contourColorRed</b> Componente vermelha da cor de contorno da geometria, valores vï¿½lidos no intervalo (0-255).
	 * - <b>contourColorGreen</b> Componente verde da cor de contorno da geometria, valores vï¿½lidos no intervalo (0-255).
	 * - <b>contourColorBlue</b> Componente azul da cor de contorno da geometria, valores vï¿½lidos no intervalo (0-255).
	 * - <b>contourStyleId</b> Estilo do contorno de poligonos: 
	 *  	; 0 = linha continua
	 *  	; 1 = tracejada
	 *  	; 2 = pontilhada
	 *  	; 3 = traï¿½o ponto
	 *  	; 4 = traï¿½o ponto ponto
	 * - <b>contourTransparency</b> Cor da linha de contorno aceita valores no intervalo (0-100), medida de porcentagem, para aplicar nï¿½vel de transparï¿½ncia.
	 * - <b>width</b> Largura da linha de contorno do poligono.
	 * 
	 *  <b>	PARA LINES </b>
	 *  - <b>geomRep</b> Representaï¿½ï¿½o de Lines valor = 2
	 *  - <b>colorRed</b> Componente vermelha da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>colorGreen</b> Componente verde da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>colorBlue</b> Componente azul da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>styleId</b> Estilo das linhas:
	 *  	; 0 = linha continua
	 *  	; 1 = tracejada
	 *  	; 2 = pontilhada
	 *  	; 3 = traï¿½o ponto
	 *  	; 4 = traï¿½o ponto ponto
	 * - <b>transparency</b> Aceita valores no intervalo (0 - 100), medida de porcentagem, para aplicar nï¿½vel de transparï¿½ncia.
	 * - <b>width</b> Largura da linha.
	 * 
	 * <b>	PARA POINTS </b>
	 *  - <b>geomRep</b> Representaï¿½ï¿½o de Points valor = 4
	 *  - <b>colorRed</b> Componente vermelha da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>colorGreen</b> Componente verde da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>colorBlue</b> Componente azul da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 *  - <b>styleId</b> Estilo dos pontos:
	 *  	; 1 = estrela
	 *  	; 2 = circulo
	 *  	; 3 = X
	 *  	; 4 = quadrado
	 *  	; 5 = diamante
	 *  	; 6 = circulo vazado
	 *  	; 7 = quadrado vazado
	 *  	; 8 = diamente vazado
	 * - <b>transparency</b> Aceita valores no intervalo (0 - 100), medida de porcentagem, para aplicar nï¿½vel de transparï¿½ncia.
	 * - <b>size</b> Tamanho do ponto
	 * 
	 *  Na qual em themeGroupingMap (Quando thematicMap = true)
	 * - <b>groupingType</b> O tipo de algoritmo de classificaï¿½ï¿½o usado para agrupar os objetos geogrï¿½ficos.
	 * 		0 = Passos Iguais
	 * 		1 = Quantil
	 * 		2 = Desvio Padrï¿½o
	 * 		3 = Valor ï¿½nico
	 * 		5 = Customizado

	 *	 <b>groupingAttributeType</b> Valor que define o tipo de atributo que vai ser usado para a classificaï¿½ï¿½o Nï¿½mero (0) ou Texto (1)
	 * - <b>fields</b> Nome da coluna usada para gerar o agrupamento
	 * - <b>fromClause</b> A tabela usada como tabela de atributos, a partir da qual foi especificada a coluna no parï¿½metro jfields.
	 * - <b>linkAttr</b> Nome da coluna que permite ligar os atributos com os objetos geogrï¿½ficos referï¿½nciados pelo tema corrente.
	 * - <b>restrictionExpression</b> Clausula de filtro.
	 * - <b>precision</b> Nï¿½mero de casas decimais consideradas usada na apresentaï¿½ï¿½o dos intervalos de cada faixa gerada.
	 * - <b>stdDev</b> O coeficiente de variaï¿½ï¿½o usado para permitir a comparaï¿½ï¿½o entre as faixas geradas quando o algoritmo de agrupamento escolhido ï¿½ o desvio padrï¿½o.
	 * - <b>numSlices</b> O nï¿½mero de faixas para gerar os grupos de objetos geogrï¿½ficos. Esse parï¿½metro somente ï¿½ utilizado nos groupingType padrï¿½o (0,1,2).
	 * - <b>rampColorsMap<b> Ramp color ï¿½ uma mapa que possui booleanas que definem quais tons de cores (RGB) serï¿½o usados nas classes geradas pelo agrupamento. Esses tons serï¿½o calculados automaticamente conforme o nï¿½mero de faixas (numSlices) selecionados e os tons (RGB). Esses parï¿½metros somente sï¿½o utilizados nos groupingType padrï¿½o (0,1,2,3)   
	 * - <b>slicesList<b> Lista de mapas de faixas que serï¿½o classificadas as geometrias desenhadas. Esse parï¿½metro somente ï¿½ utilizado nos groupingType personalizado (5).
	 * - <b>from<b> Valor minimo da faixa para agrupar as geometrias
	 * - <b>to<b> Valor mï¿½ximo da faixa para agrupar as geometrias
	 * - <b>count<b> Quantidade de objetos da faixa.
	 * - <b>description<b> Descriï¿½ï¿½o da faixa (Para ser gravada na legenda)
	 * - <b>sliceColorMap<b> Mapa de cores (RGB) que serï¿½ a cor que as geometrias que forem agrupadas nessa faixa serï¿½o pintadas .
	 * 	
	 * Na qual em labelConfig (Quando thematicMap = true)
	 * - <b>field</b> Nome do campo da tabela de atributos que será usado como texto. 
	 * - <b>detectConflict</b> Habilitar ou desabilitar o controle de conflitos de texto
	 * - <b>priorityField</b> Nome do campo da tabela de atributos que será usado para definir qual label terá prioridade para exibição caso haja conflito.
	 * - <b>urbanMode</b> Habilitar ou desabilitar o controle de visualização dos textos por escala.
	 * - <b>descTextPriorityOrder</b> Variavel que define se a prioridade de sobreposição vai ser descrecente (DESC) ou crescente (ASC)
	 * - <b>minCollisionTol</b> Define a tolerância para calculo de colisão entre labels de texto
	 * - <b>colorRed</b> Componente vermelha da cor da geometria, valores vï¿½lidos no intervalo (0-255).
 	 * - <b>colorGreen</b> Componente verde da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 * - <b>colorBlue</b> Componente azul da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 * - <b>fontFamily</b> Nome da fonte ou path para fonte para ser usado para desenho do texto
	 * - <b>width</b> Tamanho da fonte que será usado para o desenho do texto.
	 * - <b>textBox</b> Mapa com o Box que deve ser usado para o calculo de conflito do texto (Teste parcial executado sem sucesso, variavel adicionada para tentativa futura)
	 * 
	 * @param x1 Valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y1 Valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param x2 Valor da longitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y2 Valor da latitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param width	Largura da ï¿½rea de desenho, em pixels, compatï¿½vel com a
	 *            largura da imagem gerada para o dispositivo de visualizaï¿½ï¿½o
	 *            (tela).
	 * @param height Altura da ï¿½rea de desenho, em pixels, compatï¿½vel com a altura
	 *            da imagem gerada para o dispositivo de visualizaï¿½ï¿½o (tela).
	 * @param keepAspectRatio Caso True ajusta as coordenadas da ï¿½rea de interesse
	 * para manter a relaï¿½ï¿½o de aspecto da imagem gerada para o dispositivo
	 * conforme os valores definidos para a largura e altura da ï¿½rea de desenho.
	 * @param from 
	 * @param linkAttr
	 * @param restrictionExpression
	 * @param imageType
	 *            Tipo de compressï¿½o usada na imagem de saï¿½da.
	 * 
	 *            <pre>
	 * 0: para compressï¿½o PNG.
	 * 1: para compressï¿½o JPEG.
	 * 2: para compressï¿½o GIF.
	 * </pre>
	 * @param opaque
	 *            Verdadeiro ou falso definido abaixo, conforme convencionado:
	 * 
	 *            <pre>
	 *  true: gerar imagem de legenda com fundo opaco.
	 *  false: para gerar a imagem de legenda com fundo transparente.
	 * </pre>
	 * @param quality
	 *            Valor numï¿½rico, definido abaixo, que representa a porcentagem
	 *            de qualidade da imagem gerada, caso a saï¿½da seja em formato
	 *            JPEG, conforme convencionado:
	 * 
	 *            <pre>
	 * intervalo vï¿½lido: 0 ~ 100
	 * </pre>
	 * @param projectionMap Projeï¿½ï¿½o desejada para que seja desenhado no canvas 
	 * 	          no formato de um HashMap Parï¿½metros da projeï¿½ï¿½o em um HashMap.
	 * 
	 *            <pre>
	 * <b>Lista padronizada de parï¿½metros (exemplo de uso):</b>
	 * 
	 * HashMap<String, Object> projectionMap = new HashMap<String, Object>();
	 * projectionMap.put("projDatum", projection.getDatum());
	 * projectionMap.put("projName", projection.getName());
	 * projectionMap.put("projLat0", projection.getLat0());
	 * projectionMap.put("projLon0", projection.getLon0());
	 * projectionMap.put("projStLat1", projection.getStlat1());
	 * projectionMap.put("projStLat2", projection.getStlat2());
	 * projectionMap.put("projScale", projection.getScale());
	 * projectionMap.put("projOffx", projection.getOffx());
	 * projectionMap.put("projOffy", projection.getOffy());
	 * projectionMap.put("projNorthHemisphere", projection.getHemNorth());
	 * </pre>
	 * @param canvasBackground  HashMap com a cor (r,g,b) de fundo do canvas que serï¿½ desenhado.
	 * 
	 * <pre>
	 * <b>Lista padronizada de parï¿½metros (exemplo de uso):</b>
	 * 
	 * HashMap<String, Object> canvasBackgroundMap = new HashMap<String, Object>();
	 * canvasBackgroundMap.put("r", 255);
	 * canvasBackgroundMap.put("g", 255);
	 * canvasBackgroundMap.put("b", 255);
	 * </pre>
	 * @param useScaleControl Permite definir a atuaï¿½ï¿½o do controle de escala sobre os temas. O
	 * controle de escala permite que as geometrias representadas por um tema
	 * sejam desenhadas em intervalos de escalas prï¿½ definidos, um intervalo por
	 * tema, melhorando o desempenho e a apresentaï¿½ï¿½o de camadas de dados que
	 * possuem nï¿½veis de detalhamento melhor definidos em escalas diferentes.
	 * Atenï¿½ï¿½o: Atua apenas nas camadas para as quais existe definiï¿½ï¿½o de
	 * intervalo de escala.
	 *  
	 * @return Um array de bytes que representa a imagem do mapa desenhado sobre
	 *         a ï¿½rea de desenho para os temas desenhados atï¿½ o momento.
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * </pre>
	 */
	public native byte[] drawThemes(Vector<HashMap<String,Object>> themesList, double x1, double y1, double x2, double y2, 
			int width, int height, boolean keepAspectRatio, int imageType, boolean opaque, int quality,
			HashMap<String,Object> projectionMap, HashMap<String, Integer> canvasBackground, boolean useScaleControl)  throws IllegalAccessException,
			InstantiationException;
	
	public native byte[] drawLegendThemes(Vector<HashMap<String,Object>> themesList, HashMap<String, Object> visualText,
			HashMap<String, Integer> canvasBackground, int width, int imageType, boolean opaque, int quality)  throws IllegalAccessException,
			InstantiationException;

	/**
	 * Retorna a matriz do TeRaster referente ao layer com representaï¿½ï¿½o
	 * matricial (Rep = 512) do tema corrente. Se o tema corrente nï¿½o ï¿½ de um
	 * layer com representaï¿½ï¿½o matricial, a matriz retornada possui todas as
	 * celulas com valor 0.0 (matriz vazia). Este mï¿½todo ï¿½ utilizado
	 * principalmente para recuperar a matriz de valores numï¿½ricos para a
	 * contruï¿½ï¿½o de uma representaï¿½ï¿½o tridimensional. Neste caso, o Raster
	 * armazenado seria de um Modelo Numï¿½rico de Terreno (MNT) de altimetria de
	 * uma ï¿½rea geogrï¿½fica. Os parametros definem qual ï¿½rea especï¿½fica do
	 * TeRaster deve ser recuperada e qual deve ser o tamanho (linhas e colunas)
	 * da matriz.
	 * 
	 * @param x1
	 *            Valor x1 do box a ser recuperado.
	 * @param y1
	 *            Valor y1 do box a ser recuperado.
	 * @param x2
	 *            Valor x2 do box a ser recuperado.
	 * @param y2
	 *            Valor y2 do box a ser recuperado.
	 * @param width
	 *            Comprimento da matriz (Quantidade de colunas).
	 * @param height
	 *            Altura da matriz (Quantidade de linhas).
	 * @param sessionId
	 *            Nï¿½mero de controle de sessï¿½o, geralmente gerado pelo servidor
	 *            de aplicaï¿½ï¿½o no momento da criaï¿½ï¿½o da sessï¿½o do usuï¿½rio,
	 *            quando a primeira requisiï¿½ï¿½o ï¿½ feita. Deve ser um
	 *            identificador ï¿½nico.
	 * @return Matriz referente ao TeRaster.
	 */
	public native double[][] getRasterMatrix(double x1, double y1, double x2,
			double y2, int width, int height, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	
	/**
	 * Recupera lista de atributos.
	 * @param sessionId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native Vector<String> getAttributesList(String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Calcula centroide da maior geometria associada ao objectId.
	 * @param objectId
	 * @param sessionId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native HashMap<String, Double> getCentroidForBiggestGeometry(String objectId, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Adiciona features no layer do tema corrente (configurar o tema corrente primeiro).
	 * 
	 * @param geoJSONFeatures Vetor de features no formato GeoJSON.
	 * @param sessionId
	 * 
	 * @return Lista de objectsIds das novas features adicionadas.
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native Vector addFeatures(Vector<String> geoJSONFeatures, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Remove features do layer do tema corrente (configurar o tema corrente primeiro).
	 * 
	 * @param objectIds Vetor de objectIds das features a serem removidas.
	 * @param sessionId 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native boolean deleteFeatures(Vector<String> objectIds, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Atualiza features no layer do tema corrente (configurar o tema corrente primeiro).
	 * As features devem estar todas com o geom_id configurado, senao nao funciona.
	 * 
	 * @param geoJSONFeatures Vetor de features no formato GeoJSON.
	 * @param sessionId
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native boolean updateFeatures(Vector<String> geoJSONFeatures, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Recupera features do layer do tema corrente, com restricao de box (configurar o tema corrente primeiro).
	 * 
	 * @return Vetor de features no formato GeoJSON.
	 * @param x1
	 *            Valor x1 do box a ser recuperado.
	 * @param y1
	 *            Valor y1 do box a ser recuperado.
	 * @param x2
	 *            Valor x2 do box a ser recuperado.
	 * @param y2
	 *            Valor y2 do box a ser recuperado.
	 * @param sessionId
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native Vector getFeaturesInBox(double x1, double y1, double x2, double y2, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Recupera features do layer do tema corrente com restricao de objectIds (configurar o tema corrente primeiro).
	 * 
	 * @return Vetor de features no formato GeoJSON.
	 * @param objectIds Vetor de objectIds especificos a serem recuperados.
	 * @param sessionId
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native Vector getFeaturesByIds(Vector<String> objectIds, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Recupera features do layer do tema corrente com restricao personalizada (configurar o tema corrente primeiro).
	 * 
	 * @return Vetor de features no formato GeoJSON.
	 * @param restriction Restricao especifica a ser aplicada ao recuperar as features
	 * @param sessionId
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native Vector getFeaturesWithRestriction(String restriction, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * Retorna uma relacao de todos os objectIds de um tema (configurar o tema corrente primeiro).
	 * 
	 * @return Vetor de objectIds.
	 * @param restriction Restricao especifica a ser aplicada ao recuperar as features
	 * @param sessionId
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native Vector getFeaturesIds(String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	/**
	 * 
	 * @param objectId
	 * @param sessionId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native boolean updateThemeBox(String sessionId)
		throws IllegalAccessException, InstantiationException;

	/**
	 * Metodo para desenho de textos usando a rotina de desenho paralelo, permitindo a execução
	 * simultânea de N threads de desenho, sem manter estado da aplicação na biblioteca.
	 * @param themeMap onde:
	 *  HashMap<String, Object> themeMap
	 * 		|_ "themeName", String themeName (opcional ou themeName ou themeId)
	 * 		|_ "themeId", Integer themeId (opcional ou themeId ou themeName)
	 * 		|_"labelConfigMap", HashMap<String, Object> themeGrouping (OPCIONAL)
	 * 			|_"field", String labelFieldName
	 * 			|_"detectConflict", boolean detectConflict
	 * 			|_"priorityField", String priorityLabelField
	 * 			|_"urbanMode", boolean urbanMode 
	 * 			|_"descTextPriorityOrder", boolean isDescTextPriorityOrder
	 * 			|_"minCollisionTol", Integer minCollisionTol
	 * 			|_"textBox", HashMap<String,Double> textBoxMap
	 * 				|_"x1", Double x1
	 * 				|_"y1", Double y1
	 * 				|_"x2", Double x2
	 * 				|_"y2", Double y2
	 * 			|_"visualMap", HashMap<String, Integer> visualMap
	 * 				|_"colorRed", Integer textColorRed   
	 * 				|_"colorGreen", Integer textColorRed
	 * 				|_"colorBlue", Integer textColorRed
	 * 				|_"fontFamily", String textColorRed
	 * 				|_"width", Integer fontSize
	 * 
	 * Na qual em labelConfig (Quando thematicMap = true)
	 * - <b>field</b> Nome do campo da tabela de atributos que será usado como texto. 
	 * - <b>detectConflict</b> Habilitar ou desabilitar o controle de conflitos de texto
	 * - <b>priorityField</b> Nome do campo da tabela de atributos que será usado para definir qual label terá prioridade para exibição caso haja conflito.
	 * - <b>urbanMode</b> Habilitar ou desabilitar o controle de visualização dos textos por escala.
	 * - <b>descTextPriorityOrder</b> Variavel que define se a prioridade de sobreposição vai ser descrecente (DESC) ou crescente (ASC)
	 * - <b>minCollisionTol</b> Define a tolerância para calculo de colisão entre labels de texto
	 * - <b>colorRed</b> Componente vermelha da cor da geometria, valores vï¿½lidos no intervalo (0-255).
 	 * - <b>colorGreen</b> Componente verde da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 * - <b>colorBlue</b> Componente azul da cor da geometria, valores vï¿½lidos no intervalo (0-255).
	 * - <b>fontFamily</b> Nome da fonte ou path para fonte para ser usado para desenho do texto
	 * - <b>width</b> Tamanho da fonte que será usado para o desenho do texto.
	 * - <b>textBox</b> Mapa com o Box que deve ser usado para o calculo de conflito do texto (Teste parcial executado sem sucesso, variavel adicionada para tentativa futura)
	 * 
	 * @param x1 Valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y1 Valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param x2 Valor da longitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param y2 Valor da latitude do ponto que representa o canto superior
	 *            direito da ï¿½rea de interesse em coordenadas da projeï¿½ï¿½o da
	 *            vista corrente.
	 * @param width	Largura da ï¿½rea de desenho, em pixels, compatï¿½vel com a
	 *            largura da imagem gerada para o dispositivo de visualizaï¿½ï¿½o
	 *            (tela).
	 * @param height Altura da ï¿½rea de desenho, em pixels, compatï¿½vel com a altura
	 *            da imagem gerada para o dispositivo de visualizaï¿½ï¿½o (tela).
	 * @param keepAspectRatio Caso True ajusta as coordenadas da ï¿½rea de interesse
	 * para manter a relaï¿½ï¿½o de aspecto da imagem gerada para o dispositivo
	 * conforme os valores definidos para a largura e altura da ï¿½rea de desenho.
	 * @param from 
	 * @param linkAttr
	 * @param restrictionExpression
	 * @param imageType
	 *            Tipo de compressï¿½o usada na imagem de saï¿½da.
	 * 
	 *            <pre>
	 * 0: para compressï¿½o PNG.
	 * 1: para compressï¿½o JPEG.
	 * 2: para compressï¿½o GIF.
	 * </pre>
	 * @param opaque
	 *            Verdadeiro ou falso definido abaixo, conforme convencionado:
	 * 
	 *            <pre>
	 *  true: gerar imagem de legenda com fundo opaco.
	 *  false: para gerar a imagem de legenda com fundo transparente.
	 * </pre>
	 * @param quality
	 *            Valor numï¿½rico, definido abaixo, que representa a porcentagem
	 *            de qualidade da imagem gerada, caso a saï¿½da seja em formato
	 *            JPEG, conforme convencionado:
	 * 
	 *            <pre>
	 * intervalo vï¿½lido: 0 ~ 100
	 * </pre>
	 * @param projectionMap Projeï¿½ï¿½o desejada para que seja desenhado no canvas 
	 * 	          no formato de um HashMap Parï¿½metros da projeï¿½ï¿½o em um HashMap.
	 * 
	 *            <pre>
	 * <b>Lista padronizada de parï¿½metros (exemplo de uso):</b>
	 * 
	 * HashMap<String, Object> projectionMap = new HashMap<String, Object>();
	 * projectionMap.put("projDatum", projection.getDatum());
	 * projectionMap.put("projName", projection.getName());
	 * projectionMap.put("projLat0", projection.getLat0());
	 * projectionMap.put("projLon0", projection.getLon0());
	 * projectionMap.put("projStLat1", projection.getStlat1());
	 * projectionMap.put("projStLat2", projection.getStlat2());
	 * projectionMap.put("projScale", projection.getScale());
	 * projectionMap.put("projOffx", projection.getOffx());
	 * projectionMap.put("projOffy", projection.getOffy());
	 * projectionMap.put("projNorthHemisphere", projection.getHemNorth());
	 * </pre>
	 * @param canvasBackground  HashMap com a cor (r,g,b) de fundo do canvas que serï¿½ desenhado.
	 * 
	 * <pre>
	 * <b>Lista padronizada de parï¿½metros (exemplo de uso):</b>
	 * 
	 * HashMap<String, Object> canvasBackgroundMap = new HashMap<String, Object>();
	 * canvasBackgroundMap.put("r", 255);
	 * canvasBackgroundMap.put("g", 255);
	 * canvasBackgroundMap.put("b", 255);
	 * </pre>
	 * @param useScaleControl Permite definir a atuaï¿½ï¿½o do controle de escala sobre os temas. O
	 * controle de escala permite que as geometrias representadas por um tema
	 * sejam desenhadas em intervalos de escalas prï¿½ definidos, um intervalo por
	 * tema, melhorando o desempenho e a apresentaï¿½ï¿½o de camadas de dados que
	 * possuem nï¿½veis de detalhamento melhor definidos em escalas diferentes.
	 * Atenï¿½ï¿½o: Atua apenas nas camadas para as quais existe definiï¿½ï¿½o de
	 * intervalo de escala.
	 *  
	 * @return Um array de bytes que representa a imagem do mapa desenhado sobre
	 *         a ï¿½rea de desenho para os temas desenhados atï¿½ o momento.
	 * 
	 * <b>Prï¿½ requisitos:</b>
	 * 
	 * Conectar: mï¿½todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * </pre>
	 */
	public native byte[] drawThemeText(HashMap<String,Object> themeMap, double x1, double y1, double x2, double y2, 
			int width, int height, boolean keepAspectRatio, int imageType, boolean opaque, int quality,
			HashMap<String,Object> projectionMap, HashMap<String, Integer> canvasBackground, boolean useScaleControl)  throws IllegalAccessException,
			InstantiationException;
	
	static {
		System.loadLibrary("terrajava");
	}

}
