/**
 * Este pacote prov� acesso � biblioteca TerraManager da fam�lia TerraLib, atrav�s da camada de convers�o de tipos TerraJava (JNI).
 * @see <a href='bind-terrajava-jni.html'>TerraJava</a>
 * @author Claudio Henrique Bogossian
 * @file TerraJSP.java
 * Este � o c�digo fonte da classe TerraJSP.
 */
package br.org.funcate.terrajava.persistencia;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Vector;

/**
 * <pre>
 * Esta classe armazena chamadas a m�todos nativos implementados na classe TerraJava (JNI), permitindo a implementa��o
 * de sistemas de informa��es geogr�ficas na plataforma JAVA mantendo o estilo de programa��o do paradigma da orienta��o a objetos.
 * A classe TerraJSP fornece m�todos para estabelecimento de uma conex�o a um servidor de bancos de dados, explora��o do
 * conte�do do banco e um canvas (abstra��o de uma �rea para desenho) que pode ser utilizado para visualizar a
 * componente espacial dos objetos geogr�ficos do banco de dados. O desenho sobre o canvas pode ser materializado atrav�s
 * de imagens no formato PNG, JPEG ou GIF.
 * 
 * Atrav�s desta interface de programa��o � poss�vel realizar:
 * 
 * 1- Explora��o do conte�do geogr�fico e alfanumerico armazenado em um banco de dados relacional modelo TerraLib;
 * 2- Opera��es de georreferenciamento de camadas de dados vetoriais cujo conte�do representa a malha vi�ria de uma localidade;
 * 3- Cria��o de camadas de dados geogr�ficos, denominados "Layers", atrav�s de processo de importa��o de arquivo ShapeFile;
 * 4- Exporta��o de camadas de dados geogr�ficos vetoriais para arquivo em formato ShapeFile, formato de transporte de dados
 * geogr�ficos desenvolvido pela empresa ESRI - Environmental Systems Research Institute;
 * 5- Recupera��o de mapas baseados nas camadas de dados dispon�veis na base de dados TerraLib nos formatos de compress�o
 * de imagens JPEG (Joint Photographic Experts Group), PNG (Portable Network Graphics format) e GIF (Graphics Interchange Format);
 * 6- Gera��o de mapas tem�ticos baseado em algoritmos de classifica��o baseado em um atributo especifico relacionado �s geometrias de uma
 * camada de dados vetorial.
 * 
 * Antes de detalhar os m�todos dispon�veis na classe TerraJSP � preciso falar de tr�s conceitos empregados no projeto da extens�o:
 * vista corrente, temas ativos e representa��o corrente. A vista corrente define a proje��o em que todos os temas e objetos do banco
 * de dados ser�o visualizados. Ela define tamb�m os temas que estar�o dispon�veis para visualiza��o e consulta. Qualquer m�todo que
 * receba valores de coordenadas como par�metro (drawPoint, drawText, drawBox entre outros) assumir� que as coordenadas encontram-se
 * na mesma proje��o da vista corrente. Para come�ar a desenhar um tema ou objeto de um tema, � necess�rio ter uma vista corrente ativada
 * (m�todo setCurrentView).
 * 
 * Os temas ativos s�o utilizados nas opera��es de localiza��o de objetos e desenho no canvas. Pode-se ter no m�ximo dois temas ativos
 * por vez:
 * 
 * tema corrente: define o tema no qual certas opera��es dever�o ser realizadas. Somente os temas correntes podem ser desenhados sobre
 * o canvas. Esta documenta��o explicita em cada m�todo os pr� requisitos para a sua utiliza��o. Ter um tema corrente definido �
 * pr� requisito para v�rios m�todos.
 * 
 * tema de refer�ncia: geralmente utilizado como refer�ncia para localiza��o de objetos relacionados com o tema corrente.
 * Por exemplo, se no tema corrente temos objetos que representam queimadas (pontos) e no tema de refer�ncia temos objetos que
 * representam munic�pios (pol�gonos), poder�amos realizar a seguinte consulta espacial: "localizar o munic�pio que cont�m um determinado
 * ponto de queimada".
 * 
 * O �ltimo conceito importante � o de representa��o corrente. Para cada um dos tipos de tema (corrente e refer�ncia) � poss�vel
 * indicar quais as respectivas representa��es geom�tricas ativas, pontos, linhas e poligonos. Estas representa��es afetam as opera��es
 * espaciais e o desenho de objetos selecionados.
 * </pre>
 * 
 * @author Claudio Henrique Bogossian
 * @version 1.0, 04/03/2009
 */

public class TerraJava {

	/**
	 * M�todo nativo para conectar-se ao banco de dados modelo TerraLib.
	 * 
	 * @param host
	 *            Nome ou IP do servidor onde roda o servi�o do gerenciador de
	 *            banco de dados.
	 * @param user
	 *            Nome do usu�rio com permiss�o de acesso ao banco de dados. �
	 *            necess�ria a permiss�o de leitura e escrita no banco, para o
	 *            uso de fun��es de importa��o de dados e cria��o de "layers".
	 * @param password
	 *            Senha do usu�rio para valida��o do acesso.
	 * @param database
	 *            Nome do banco de dados.
	 * @param port
	 *            N�mero da porta usada pelo gerenciador de banco de dados para
	 *            receber conex�es.
	 * @param dbType
	 *            Tipo de gerenciador de banco de dados. Par�metro usado para
	 *            selacionar o driver correto para a conex�o.
	 * 
	 *            <pre>
	 * Os tipos suportados s�o:
	 * 
	 * TeMySQLDB = 1,           Para conex�o com MySQL
	 * TePostgreSQLDB = 2,      Para conex�o com PostgreSQL que n�o possua a extens�o espacial PostGIS
	 * TePostGISDB = 3,         Para conex�o com PostgreSQL que possua a extens�o espacial PostGIS
	 * TeADODB = 4,             Para conex�o com Microsoft Access (only in Windows).
	 * TeADOOracleDB = 5,       Para conex�o com Oracle usando Microsoft ADO support (only in Windows).
	 * TeADOSqlServerDB = 6,    Para conex�o com SQL Server (only in Windows).
	 * TeOracleSpatialDB = 7,   Para conex�o com Oracle Spatial usando o driver OCI
	 * TeOracleOCIDB = 8,       Para conex�o com Oracle sem suporte espacial usando o driver OCI
	 * TeIBFirebirdDB = 9       Para conex�o com Firebird
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) caso tenha sucesso na conex�o e Falso (false)
	 *         caso contr�rio.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou n�o seja poss�vel conectar ao banco.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * N�o p�de encontrar o host, ou a autentica��o fornecida por usu�rio e senha � inv�lida,
	 * o banco especificado pelo par�metro database n�o existe ou n�o pode ser acessado,
	 * ou ainda o modelo de dados TerraLib � antigo, diferente de 3.3.1, e portanto n�o suportado.
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
	 * <b>Pr� requisitos:</b>
	 * 
	 * A exist�ncia de um banco de dados geogr�fico modelo TerraLib.
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
	 * M�todo nativo para configurar como corrente uma determinada vista
	 * existente no banco de dados para o usu�rio usado para conectar. A
	 * proje��o definida para a vista ser� adotada como padr�o para todas as
	 * opera��es de desenho de objetos geogr�ficos sobre o canvas.
	 * 
	 * @param view
	 *            Nome de uma vista existente no banco de dados e que tenha sido
	 *            criada pelo usu�rio fornecido pelo par�metro userName. As
	 *            vistas s�o de propriedade do usu�rio que as criou.
	 * @param userName
	 *            nome do usu�rio dono da vista selecionada para as opera��es
	 *            que exigem uma vista corrente como pr� requisito.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true), se foi bem sucedido na opera��o de sele��o da
	 *         vista como vista corrente ou Falso (false) caso contr�rio.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou n�o seja poss�vel configurar como corrente a
	 *             vista fornecida.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * A vista especificada n�o existe ou n�o � de propriedade do usu�rio usado na solicita��o, par�metro userName,
	 * ou ainda o par�metro view foi passado como uma String vazia.
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
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Conhecer o nome de uma vista v�lida.
	 * Conhecer o nome do usu�rio dono da vista escolhida.
	 * </pre>
	 */
	public native boolean setCurrentView(String view, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * M�todo nativo para acessar o nome da vista corrente.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return O nome da vista corrente.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou n�o seja poss�vel recuperar a vista corrente.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * N�o existe uma vista definida como corrente.
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
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * </pre>
	 * 
	 */
	public native String getCurrentView(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * M�todo nativo para definir uma tema como corrente ou refer�ncia. O tema
	 * corrente pode ser desenhado no canvas, j� o tema de refer�ncia � usado
	 * para opera��es de restri��o ou sele��o espacial e opera��es topol�gicas.
	 * 
	 * @param theme
	 *            Nome de um tema v�lido, existente na �rvore de temas da vista
	 *            corrente.
	 * @param themeType
	 *            Tipo de defini��o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de refer�ncia.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true), se foi bem sucedido na opera��o de sele��o do
	 *         tema como tema corrente ou de refer�ncia ou Falso (false) caso
	 *         contr�rio.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou n�o seja poss�vel definir o tema corrente ou de
	 *             refer�ncia.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * O parametro theme, � um nome de tema que n�o existe na �rvore de temas da vista corrente.
	 * O par�metro theme � uma string vazia.
	 * O tema encontrado n�o possui uma representa��o vetorial ativa.
	 * Entende-se como representa��o vetorial, os tipos de dados vetoriais basicos definidos pela TerraLib: c�lulas, poligonos, linhas, pontos e texto.
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
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("refer�ncia"))+".";
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * </pre>
	 * 
	 */
	public native boolean setTheme(String theme, int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * M�todo nativo para definir uma tema como corrente ou refer�ncia. O tema
	 * corrente pode ser desenhado no canvas, j� o tema de refer�ncia � usado
	 * para opera��es de restri��o ou sele��o espacial e opera��es topol�gicas.
	 * 
	 * @param theme
	 *            Nome de um tema v�lido, existente na �rvore de temas da vista
	 *            corrente.
	 * @param themeType
	 *            Tipo de defini��o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de refer�ncia.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true), se foi bem sucedido na opera��o de sele��o do
	 *         tema como tema corrente ou de refer�ncia ou Falso (false) caso
	 *         contr�rio.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou n�o seja poss�vel definir o tema corrente ou de
	 *             refer�ncia.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * O parametro theme, � um nome de tema que n�o existe na �rvore de temas da vista corrente.
	 * O par�metro theme � uma string vazia.
	 * O tema encontrado n�o possui uma representa��o vetorial ativa.
	 * Entende-se como representa��o vetorial, os tipos de dados vetoriais basicos definidos pela TerraLib: c�lulas, poligonos, linhas, pontos e texto.
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
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * </pre>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public native boolean setThemesPriorityOrder(Vector themeList,
			boolean persist, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * M�todo nativo para acessar o nome do tema definido como corrente ou
	 * refer�ncia.
	 * 
	 * @param themeType
	 *            Tipo de defini��o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de refer�ncia.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Acesso ao nome do tema definido previamente como tema corrente ou
	 *         de refer�ncia.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou n�o seja poss�vel acessar o tema corrente ou de
	 *             refer�ncia.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * N�o foi definida uma vista corrente.
	 * N�o existe um tema definido como corrente ou de refer�ncia.
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
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("refer�ncia"))+".";
	 *    // recuperando o tema corrente.
	 *    String currentTheme = terraJSP.getTheme(themeType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente ou de refer�ncia: {@link #setTheme(String, int, String)}
	 * </pre>
	 * 
	 */
	public native String getTheme(int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * ! Este m�todo deve ser excluido. A assinatura n�o necessita dos
	 * par�metros Vector vectorObj e Double doubleObj
	 */
	@SuppressWarnings("unchecked")
	private native Vector getThemeBox(int themeType, String restriction,
			Vector vectorObj, Double doubleObj, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * M�todo nativo que permite obter as coordenadas do ret�ngulo envolvente
	 * dos objetos do tema corrente ou de refer�ncia levando em considera��o a
	 * restri��o se esta tiver sido definida.
	 * 
	 * @param themeType
	 *            Tipo de defini��o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de refer�ncia.
	 * </pre>
	 * @param restriction
	 *            Permite aplicar uma restri��o ao conjunto de objetos
	 *            geogr�ficos apontados pelo tema corrente ou de refer�ncia. O
	 *            formato permitido para esta restri��o � uma <b>clausula
	 *            <i>where</i></b> no formato SQL ANSI. A restri��o deve seguir
	 *            o formato
	 *            "nome_da_coluna + operador + valor ou lista_de_valores".
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Um vetor de quatro elementos float contendo as coordenadas do
	 *         ret�ngulo envolvente dos objetos do tema corrente ou de
	 *         refer�ncia (x1,x2,y1,y2 respectivamente). Os valores de
	 *         coordenadas encontram-se no sistema de proje��o indicado pela
	 *         vista corrente.
	 * 
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou a restri��o tenha gerado um erro na clausula
	 *             <i>where</i> de filtro.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * N�o foi definida uma vista corrente.
	 * N�o existe um tema definido como corrente ou de refer�ncia.
	 * O tema definido como corrente ou de refer�ncia n�o possui representa��o vetorial ativa.
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
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("refer�ncia"))+".";
	 *    box = terraJSP.getThemeBox(themeType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente ou de refer�ncia: {@link #setTheme(String, int, String)}
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
	 * M�todo nativo para manipula��o do canvas, que permite ajustar o tamanho,
	 * largura e altura, da �rea de desenho compativel com o dispositivo de
	 * sa�da, tela, e o box da �rea de interesse, em coordenadas da proje��o da
	 * vista corrente. Este m�todo ajusta as coordenadas da �rea de interesse
	 * para manter a rela��o de aspecto da imagem gerada para o dispositivo
	 * conforme os valores definidos para a largura e altura da �rea de desenho.
	 * O uso deste m�todo � pr� requisito nas opera��es de desenho de dados
	 * geogr�ficos na �rea de desenho.<br/>
	 * Aten��o: O processo de reconfigura��o do canvas usando este m�todo limpa
	 * a �rea de desenho, e tudo que foi desenhado at� o momento ser� perdido.
	 * 
	 * @see <a href="#drawCurrentTheme(java.lang.String)">drawCurrentTheme</a>
	 * @see <a
	 *      href="#drawCurrentThemeLegend(java.lang.String, int, int, boolean, int, java.lang.String)">drawCurrentThemeLegend</a>
	 * @see <a
	 *      href="#drawGroupSqlAndLegend(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, boolean, boolean, boolean, int, int, int, java.lang.String)">drawGroupSqlAndLegend</a>
	 *      e todos os demais que iniciam processo de desenho sobre o canvas,
	 *      m�todos cujo prefixo � <b>draw</b>
	 * @see <a
	 *      href="#locateObject(double, double, double, java.lang.String)">locateObject</a>
	 * 
	 * @param xmin
	 *            valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param ymin
	 *            valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param xmax
	 *            valor da longitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param ymax
	 *            valor da latitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param width
	 *            Largura da �rea de desenho, em pixels, compat�vel com a
	 *            largura da imagem gerada para o dispositivo de visualiza��o
	 *            (tela).
	 * @param height
	 *            Altura da �rea de desenho, em pixels, compat�vel com a altura
	 *            da imagem gerada para o dispositivo de visualiza��o (tela).
	 * @param keepAspectRatio
	 * 			  Caso True ajusta as coordenadas da �rea de interesse
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Um vetor com o box da �rea de interesse, ajustado conforme a
	 *         largura e altura definidas para a �rea de desenho (canvas).
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou o box da �rea de interesse seja inv�lido.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * N�o foi definida uma vista corrente.
	 * Valores xmin, xmax, ymin ou ymax maiores que o valor m�ximo permitido para vari�veis tipo float. 
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
	 *       // definindo o box da �rea de interesse, e o tamanho da �rea de desenho. 
	 *       box = terraJSP.setWorld(box[0], box[1], box[2], box[3], width, height, sessionId);
	 *    }
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Conhecer um box v�lido, que intercepte o box da vista corrente.
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
	 * M�todo nativo que permite desenhar na �rea de desenho, canvas, o conte�do
	 * geogr�fico representado por um tema. Este conte�do pode ser de natureza
	 * vetorial ou matricial. No caso dos dados serem vetoriais a opera��o de
	 * desenho considera os estilos na seguinte sequ�ncia:
	 * 
	 * <pre>
	 * 1� Estilo definido pela camada de aplica��o, atrav�s dos m�todos <a href="#setThemeVisualLine(int, int, int, int, int, int, boolean, java.lang.String)">setThemeVisualLine</a>, <a href="#setThemeVisualPoint(int, int, int, int, int, boolean, java.lang.String)">setThemeVisualPoint</a>, <a href="#setThemeVisualPolygon(int, int, int, int, int, int, int, int, int, int, int, boolean, java.lang.String)">setThemeVisualPolygon</a>, <a href="#setThemeVisualText(int, int, int, int, int, int, int, java.lang.String, boolean, java.lang.String)">setThemeVisualText</a>, <a href="#setThemeVisualText(int, int, int, int, int, int, int, java.lang.String, boolean, boolean, double, double, int, int, boolean, java.lang.String)">setThemeVisualText</a>
	 * 2� Estilo padr�o definido para o tema, persistido no banco de dados.
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return uma String com a lista de par�metros da legenda do tema
	 *         desenhado:
	 *         representation-style-R-G-B-transparency-width-ContourStyle
	 *         -ContourR-ContourG-ContourB-ContourWidth-pointsize-from-to-label
	 *         alterar este retorno em futuro pr�ximo, retornar a lista em
	 *         estrutura Vector.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos.
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
	 * 
	 * Uma conex�o n�o foi criada com o banco de dados.
	 * N�o foi definida uma vista corrente.
	 * N�o existe um tema definido como corrente.
	 * O canvas n�o foi preparado para iniciar um processo de desenho, @see <a href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
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
	 *       // definindo o box da �rea de interesse, e o tamanho da �rea de desenho. 
	 *       box = terraJSP.setWorld(box[0], box[1], box[2], box[3], width, height, sessionId);
	 *       // definindo o tema corrente a ser desenhado
	 *       if(!terraJSP.setTheme(theme, themeType, sessionId))
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("refer�ncia"))+".";
	 *    }
	 *    // desenhando o conte�do geogr�fico do tema corrente
	 *    terraJSP.drawCurrentTheme(sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente: {@link #setTheme(String, int, String)}
	 * Definir uma �rea de interesse e um tamanho para a imagem de sa�da: {@link #setWorld(double, double, double, double, int, int, String)}
	 * </pre>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public native Vector<HashMap> drawCurrentTheme(String sessionId)
			throws IllegalAccessException, InstantiationException;


	/**
	 * M�todo nativo que permite gerar uma imagem a partir da �rea de desenho,
	 * canvas, nos formatos de compress�o PNG, JPEG e GIF. Pode-se solicitar que
	 * o plano de fundo da �rea de desenho seja transparente ou opaco. A imagem
	 * de sa�da ser� uma reprodu��o fiel do que foi desenhado sobre a �rea de
	 * desenho, dados vetoriais e matriciais, no tamanho especificado pelo
	 * m�todo setWorld.
	 * 
	 * @see <a
	 *      href="#setWorld(double, double, double, double, int, int, java.lang.String)">setWorld</a>
	 * 
	 * @param imageType
	 *            Tipo de compress�o usada na imagem de sa�da.
	 * 
	 *            <pre>
	 * 0: para compress�o PNG.
	 * 1: para compress�o JPEG.
	 * 2: para compress�o GIF.
	 * </pre>
	 * @param isOpaque
	 *            Verdadeiro ou falso definido abaixo, conforme convencionado:
	 * 
	 *            <pre>
	 *  true: gerar imagem de legenda com fundo opaco.
	 *  false: para gerar a imagem de legenda com fundo transparente.
	 * </pre>
	 * @param quality
	 *            Valor num�rico, definido abaixo, que representa a porcentagem
	 *            de qualidade da imagem gerada, caso a sa�da seja em formato
	 *            JPEG, conforme convencionado:
	 * 
	 *            <pre>
	 * intervalo v�lido: 0 ~ 100
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Um array de bytes que representa a imagem do mapa desenhado sobre
	 *         a �rea de desenho para os temas desenhados at� o momento.
	 * @throws Exception
	 *             Lan�a exce��o caso os pr� requisitos n�o tenham sido
	 *             obedecidos ou
	 * 
	 *             <pre>
	 * <b>As causas poss�veis s�o:</b>
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
	 *          mensagemErro = "Falhou ao configurar o tema "+theme+" como "+((themeType==0)?("corrente"):("refer�ncia"))+".";
	 *    theme = terraJSP.getTheme(themeType, sessionId);
	 * }catch (Exception e) {
	 *    mensagemErro = "Erro ao recuperar a vista corrente: ";
	 *    mensagemErro += terraJSP.errorMessage(sessionId);
	 *    throw new TerraJavaDAOException(mensagemErro + e);
	 * }
	 * </div>
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
	 * Definir uma vista corrente: setCurrentView() {@link #setCurrentView(String, String, String)}
	 * Definir um tema corrente ou de refer�ncia: {@link #setTheme(String, int, String)}
	 * </pre>
	 * 
	 */
	public native byte[] getCanvasImage(int imageType, boolean isOpaque,
			int quality, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * M�todo nativo que permite salvar em disco uma imagem a partir da �rea de
	 * desenho, canvas, no formato de compress�o PNG.
	 * 
	 * @param fileName
	 *            Diret�rio onde a imagem dever� ser salva, caminho completo, e
	 *            nome do arquivo e ser gerado incluindo a extens�o.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) caso tenha sucesso e falso (false) caso
	 *         contr�rio.
	 */
	public native boolean saveCanvasImage(String fileName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * M�todo nativo que permite recuperar a mensagem do �ltimo erro ocorrido.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Mensagem de erro no formato alfanum�rico, String.
	 */
	public native String errorMessage(String sessionId)
			throws IllegalAccessException, InstantiationException;

	@SuppressWarnings("unchecked")
	private native Vector getViews(String user, Vector vectorObj,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * M�todo nativo que permite recuperar a lista de vistas existente no banco
	 * de dados ao qual se est� conectado e que tenham sido criadas pelo usu�rio
	 * especificado pelo par�metro user.
	 * 
	 * @param user
	 *            Nome de um usu�rio v�lido com permiss�es de acesso ao banco de
	 *            dados.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Lista de vistas do usu�rio especificado, em estrutura Vector.
	 */
	@SuppressWarnings("unchecked")
	public Vector getViews(String user, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return getViews(user, new Vector(), sessionId);
	}

	/**
	 * M�todo nativo que permite recuperar a imagem da legenda no formato PNG,
	 * desenhada pelo m�todo drawLegend.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 * ret�ngulo envolvente dos objetos de todos os temas da vista corrente (x1,
	 * y1, x2 e y2 respectivamente). Os valores das coordenadas encontram-se no
	 * sistema de proje��o indicada pela vista corrente.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 * Desenha um ret�ngulo sobre o canvas com possibilidade de ser preenchido
	 * ou n�o. As coordenadas do ret�ngulo devem estar no sistema de proje��o da
	 * vista corrente.
	 * 
	 * @param x1
	 *            valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y1
	 *            valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param x2
	 *            valor da longitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y2
	 *            valor da latitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param fill
	 *            Define se o ret�ngulo deve ser preenchido ou n�o.
	 * 
	 *            <pre>
	 * 0: sem preenchimento.
	 * 1: com preenchimento.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel desenhar o ret�ngulo solicitado
	 *         e falso (false) caso contr�rio.
	 */
	private native boolean drawBox(double x1, double y1, double x2, double y2,
			int fill, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Desenha um ret�ngulo sobre o canvas sem preenchimento. As coordenadas do
	 * ret�ngulo devem estar no sistema de proje��o da vista corrente.
	 * 
	 * @param x1
	 *            valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y1
	 *            valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param x2
	 *            valor da longitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y2
	 *            valor da latitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel desenhar o ret�ngulo solicitado
	 *         e falso (false) caso contr�rio.
	 */
	public boolean drawBox(double x1, double y1, double x2, double y2,
			String sessionId) throws IllegalAccessException,
			InstantiationException {
		return drawBox(x1, y1, x2, y2, 0, sessionId);
	}

	/**
	 * Desenha r�tulo de texto sobre geometrias com representa��o de linha no
	 * �ngulo da tangente gerada no ponto de desenho do texto com o seguimento
	 * de linha e o eixo das ordenadas. O r�tulo � extra�do de uma das colunas
	 * da tabela de atributos. A opera��o de desenho pode executar um algoritmo
	 * de verifica��o de conflito. O contexto para a gera��o da camada de texto
	 * sobre o mapa � configurado pelos m�todos:
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel desenhar os r�tulos e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean drawLineAngleTextLabeling(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Desenha r�tulo de texto sobre geometrias com de qualquer representa��o na
	 * horizontal. O r�tulo � extra�do de uma das colunas da tabela de
	 * atributos. A opera��o de desenho pode executar um algoritmo de
	 * verifica��o de conflito. O contexto para a gera��o da camada de texto
	 * sobre o mapa � configurado pelos m�todos:
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel desenhar os r�tulos e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean drawHorizontalTextLabeling(String restrExp, String sessionId)
			throws IllegalAccessException, InstantiationException;
	
	public boolean drawHorizontalTextLabeling(String sessionId) 
			throws IllegalAccessException, InstantiationException {
		return drawHorizontalTextLabeling("", sessionId);
	}

	/**
	 * Retorna as representa��es ativas do tema corrente ou do tema de
	 * refer�ncia.
	 * 
	 * @return Valor num�rico inteiro conforme a lista:
	 * 
	 *         <pre>
	 * 1 Pol�gonos
	 * 2 Linhas
	 * 4 Pontos
	 * 128 Texto
	 * 256 C�lulas
	 * 512 Raster.
	 * </pre>
	 */
	public native int getThemeRepresentation(int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Dado um identificador de objeto ou lista de identificadores de objeto de
	 * um tema, gera um mapa de dist�ncia ao redor do(s) objeto(s) indicado(s) e
	 * desenha-os em seguida.
	 * 
	 * @param Vector
	 *            <String> Oids, Vetor de objectIds do tipo: java/lang/String
	 * @param double distance, ajuste de dist�ncia para a cria��o do buffer em
	 *        torno da(s) geometrias(s) encontradas para os identificadores em
	 *        Oids. O valor zero (distance=0) n�o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precis�o, e MENOR o desempenho. Usado
	 *        apenas na gera��o de buffer.
	 * @param boolean unionPoly, executar uni�o dos poligonos de entrada com os
	 *        buffers
	 * @param int themeType, tema corrente ou de refer�ncia.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return true caso desenhe com sucesso e false caso contr�rio.
	 * 
	 **/
	public native boolean drawBufferZoneWithOids(Vector<String> Oids,
			double distance, int bufferType, int numPoints, boolean unionPoly,
			int themeType, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado um identificador de objeto ou lista de identificadores de objeto de
	 * um tema, gera um mapa de dist�ncia ao redor do(s) objeto(s) indicado(s) e
	 * desenha-os em seguida.
	 * 
	 * @param Vector
	 *            <Point2D.Double> points, Vetor de pontos do tipo:
	 *            java/awt/geom/Point2D.Double
	 * @param double distance, ajuste de dist�ncia para a cria��o do buffer em
	 *        torno da(s) geometrias(s) encontradas para os identificadores em
	 *        Oids. O valor zero (distance=0) n�o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precis�o, e MENOR o desempenho. Usado
	 *        apenas na gera��o de buffer.
	 * @param boolean unionPoly, executar uni�o dos poligonos de entrada com os
	 *        buffers
	 * @param int themeType, tema corrente ou de refer�ncia.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return true caso desenhe com sucesso e false caso contr�rio.
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
	 * M�todo de acesso aos nome dos temas da vista corrente.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Lista de nomes dos temas da vista corrente em formato Vector.
	 */
	@SuppressWarnings("unchecked")
	public Vector getThemes(String sessionId) throws IllegalAccessException,
			InstantiationException {
		return getThemes(new Vector(), false, sessionId);
	}

	/**
	 * Consulta por apontamento. Retorna o identificador do objeto que cont�m um
	 * determinado ponto. Em opera��es de localiza��o de objetos geogr�ficos
	 * vetoriais pela fun��o locateObject, o calculo da tolerancia � ajustado
	 * conforme o box da �rea de interesse em rela��o ao tamanho da imagem de
	 * sa�da do dispositivo de visualiza��o, tornando impresind�vel o ajuste da
	 * �rea de desenho, canvas, com o box da �rea de interesse atrav�s do uso do
	 * m�todo setWorld().
	 */
	@SuppressWarnings("unchecked")
	public native Vector locateObject(double x, double y, double tol,
			int themeType, boolean storeGeom, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Retorna o identificador de um objeto que cont�m um determinado ponto,
	 * levando em considera��o uma toler�ncia em pixel. Pode operar sobre o tema
	 * corrente ou de refer�ncia. Geralmente usado em consulta por apontamento,
	 * onde temos um ponto sobre a �rea de interesse. Em opera��es de
	 * localiza��o de objetos geogr�ficos vetoriais pela fun��o locateObject, o
	 * calculo da tolerancia � ajustado conforme o box da �rea de interesse em
	 * rela��o ao tamanho da imagem de sa�da do dispositivo de visualiza��o,
	 * tornando impresind�vel o ajuste da �rea de desenho, canvas, com o box da
	 * �rea de interesse atrav�s do uso do m�todo setWorld().
	 * 
	 * @param x
	 *            Valor da coordenada do ponto de interesse, longitude, no
	 *            sistema de proje��es da vista corrente.
	 * @param y
	 *            Valor da coordenada do ponto de interesse, latitude, no
	 *            sistema de proje��es da vista corrente.
	 * @param tol
	 *            Valor da toler�ncia requisitada para opera��o de cruzamento
	 *            entre o ponto solicitado e as geometrias presentes na camada
	 *            pesquisada, representadas pelo tema corrente ou de refer�ncia.
	 * @param themeType
	 *            Defini��o de tema a ser pesquisado, corrente ou de refer�ncia.
	 * 
	 *            <pre>
	 * 0: tema corrente.
	 * 1: tema de refer�ncia.
	 * </pre>
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 * satisfa�am um certo relacionamento topol�gico sobre as geometrias do tema
	 * refer�ncia.
	 * 
	 * @param Vector
	 *            <String> Oids, Vetor de objectIds do tipo: java/lang/String
	 * @param int relation, relacionamento topol�gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrep�em 32 Cont�m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de dist�ncia para a cria��o do buffer em
	 *        torno da(s) geometrias(s) encontradas para os identificadores em
	 *        Oids. O valor zero (distance=0) n�o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precis�o, e MENOR o desempenho. Usado
	 *        apenas na gera��o de buffer.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         opera��o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithOids(Vector<String> Oids,
			int relation, double distance, int bufferType, int numPoints,
			boolean unionPoly, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado um ponto ou lista de pontos, retorna a lista de objetos
	 * (identificadores) que satisfa�am um certo relacionamento topol�gico sobre
	 * as geometrias do tema corrente.
	 * 
	 * @param Vector
	 *            <Point2D.Double> aListPoints, Vetor de pontos do tipo:
	 *            java/awt/geom/Point2D.Double
	 * @param int relation, relacionamento topol�gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrep�em 32 Cont�m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de dist�ncia para a cria��o do buffer em
	 *        torno do(s) ponto(s) em aListPoints. O valor zero (distance=0) n�o
	 *        gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precis�o, e MENOR o desempenho. Usado
	 *        apenas na gera��o de buffer.
	 * @param int themeType, indica��o do tema de pesquisa, corrente ou
	 *        refer�ncia:
	 * 
	 *        0: tema corrente. 1: tema de refer�ncia.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         opera��o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithPoints(
			Vector<Point2D.Double> aListPoints, int relation, double distance,
			int bufferType, int numPoints, int themeType, boolean unionPoly,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado uma linha ou lista de linhas, retorna a lista de objetos
	 * (identificadores) que satisfa�am um certo relacionamento topol�gico sobre
	 * as geometrias do tema corrente.
	 * 
	 * @param Vector
	 *            <Vector<Point2D.Double>> aListLines, Vetor de pontos do tipo:
	 *            java/awt/geom/Point2D.Double
	 * @param int relation, relacionamento topol�gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrep�em 32 Cont�m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de dist�ncia para a cria��o do buffer em
	 *        torno da(s) linha(s) em aListLines. O valor zero (distance=0) n�o
	 *        gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precis�o, e MENOR o desempenho. Usado
	 *        apenas na gera��o de buffer.
	 * @param int themeType, indica��o do tema de pesquisa, corrente ou
	 *        refer�ncia:
	 * 
	 *        0: tema corrente. 1: tema de refer�ncia.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         opera��o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithLines(
			Vector<Vector<Point2D.Double>> aListLines, int relation,
			double distance, int bufferType, int numPoints, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado uma linha ou lista de linhas, retorna a lista de objetos
	 * (identificadores) que satisfa�am um certo relacionamento topol�gico sobre
	 * as geometrias do tema corrente.
	 * 
	 * @param Vector
	 *            <Vector<Vector<Point2D.Double>>> aListPolygons, Vetor de
	 *            polygons do tipo: java/awt/geom/Point2D.Double
	 * @param int relation, relacionamento topol�gico:
	 * 
	 *        1 Disjunto 2 Toca 4 Cruza 8 Dentro 16 Sobrep�em 32 Cont�m 64
	 *        Intercepta 128 Igual 256 Cobre 512 Coberto por
	 * 
	 * @param double distance, ajuste de dist�ncia para a cria��o do buffer em
	 *        torno do(s) polygon(s) em aListPolygons. O valor zero (distance=0)
	 *        n�o gera buffer.
	 * @param int bufferType, tipo de buffer a ser calculado:
	 * 
	 *        0: Usado para criar um buffer somente para fora da fronteira do
	 *        objeto. 1: Usado para criar um buffer somente para dentro da
	 *        fronteira do objeto. 2: Usado para criar um buffer para fora e
	 *        para dentro da fronteira do objeto.
	 * 
	 * @param int numPoints, ajuste fino para melhorar a qualidade do buffer:
	 *        quanto mais pontos MAIOR a precis�o, e MENOR o desempenho. Usado
	 *        apenas na gera��o de buffer.
	 * @param int themeType, indica��o do tema de pesquisa, corrente ou
	 *        refer�ncia:
	 * 
	 *        0: tema corrente. 1: tema de refer�ncia.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Vector<String>, lista de objectIds das geometrias encontradas na
	 *         opera��o.
	 * 
	 **/
	public native Vector<String> locateObjectsWithPolygons(
			Vector<Vector<Vector<Point2D.Double>>> aListPolygons, int relation,
			double distance, int bufferType, int numPoints, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Dado um identificador de um objeto do tema corrente, retorna a lista de
	 * objetos (identificadores) do tema de refer�ncia que satisfa�am um certo
	 * relacionamento topol�gico (toca, cruza, sobrep�e, dentro, cont�m,
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
	 * M�todo nativo que permite recuperar a lista de atributos de uma geometria
	 * do tema carregado como corrente ou de refer�ncia.
	 * 
	 * @param objectid
	 *            o identificador �nico da geometria para a qual se deseja ler a
	 *            lista de atributos e seus respectivos valores.
	 * @param themeType
	 *            indica��o do tema onde encontrar a geometria indicada pelo
	 *            par�metro objectid, corrente ou refer�ncia:
	 * 
	 *            <pre>
	 * 0: tema corrente.
	 * 1: tema de refer�ncia.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Lista dos atributos relacioandos para a geometria indicada, em
	 *         estrutura de dados tipo Vector, padronizado em:
	 * 
	 *         <pre>
	 * [0]=nome Da Coluna da tabela de atributos.
	 * [1]=valor do registro para a coluna indicada na posi��o anterior.
	 * [2]=nome Da Coluna da tabela de atributos.
	 * [3]=valor do registro para a coluna indicada na posi��o anterior.
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
	 * Ajusta o visual de uma determinada representa��o geom�trica a ser
	 * desenhada. Atua apenas em tempo de execu��o. Os seguintes m�todos
	 * utilizam este visual: drawSelectedObject, drawPoint, drawBox, drawTex,
	 * drawBufferZone e drawLegend.
	 * 
	 * @param rep
	 *            Representa��o geom�trica que receber� o novo visual comforme
	 *            defini��o TerraLib: c�lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: c�lulas
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria.
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria.
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria.
	 * @param style
	 *            Estilo para representa��o geom�trica.
	 * 
	 *            <pre>
	 * Ponto, linha ou poligono, observar o convencionado pelo TerraLib:
	 * 
	 * <b>Preenchimento c�lula ou poligono</b>: 0 = transparente, 1 = preenchimento opaco, 2 = hachura horizontal, 3 = hachura vertical,
	 * 4 = hachura diagonal inclina��o em 135�, 5 = hachura diagonal inclina��o em 45�,
	 * 6 = hachura horizontal e vertical, 7 = hachura horizontal e vertical inclinada em 45�
	 * <b>linha ou contorno de poligonos</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = tra�o ponto, 4 = tra�o ponto ponto
	 * <b>ponto</b>: type 1 = estrela, 2 = circulo, 3 = X, 4 = quadrado, 5 = diamante, 6 = circulo vazado, 7 = quadrado vazado, 8 = diamente vazado
	 * <b>texto</b>: define-se a fonte o tamanho e a cor.
	 * </pre>
	 * @param width
	 *            dimens�o do objeto.
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
	 *            usada no caso de representa��o de texto, nome da fonte
	 *            incluindo o caminho completo para acessa-l�. Pode ser
	 *            necess�rio verificar as permiss�es de acesso ao aqruivo de
	 *            fontes.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel configurar o estilo e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean setDefaultVisual(int rep, int red, int green,
			int blue, int style, int width, String fontName, int rcontour,
			int gcontour, int bcontour, int stylecontour, int widthcontour,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Ajusta o visual de uma determinada representa��o geom�trica a ser
	 * desenhada. � a segunda na ordem de prioridade sobre as demais defini��es
	 * de visual, sendo a SLD a primeira. Atua apenas em tempo de execu��o, n�o
	 * persiste em banco. Os seguintes m�todos utilizam este visual:
	 * drawSelectedObject, drawPoint, drawBox, drawTex, drawBufferZone e
	 * drawLegend.
	 * 
	 * @param rep
	 *            Representa��o geom�trica que receber� o novo visual comforme
	 *            defini��o TerraLib: c�lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: c�lulas
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria.
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria.
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria.
	 * @param style
	 *            Estilo para representa��o geom�trica.
	 * 
	 *            <pre>
	 * Ponto, linha ou poligono, observar o convencionado pelo TerraLib:
	 * 
	 * <b>Preenchimento c�lula ou poligono</b>: 0 = transparente, 1 = preenchimento opaco, 2 = hachura horizontal, 3 = hachura vertical,
	 * 4 = hachura diagonal inclina��o em 135�, 5 = hachura diagonal inclina��o em 45�,
	 * 6 = hachura horizontal e vertical, 7 = hachura horizontal e vertical inclinada em 45�
	 * <b>linha ou contorno de poligonos</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = tra�o ponto, 4 = tra�o ponto ponto
	 * <b>ponto</b>: 1 = estrela, 2 = circulo, 3 = X, 4 = quadrado, 5 = diamante, 6 = circulo vazado, 7 = quadrado vazado, 8 = diamente vazado
	 * <b>texto</b>: define-se a fonte o tamanho e a cor.
	 * </pre>
	 * @param width
	 *            dimens�o do objeto.
	 * @param fontName
	 *            usada no caso de representa��o de texto, nome da fonte
	 *            incluindo o caminho completo para acessa-l�. Pode ser
	 *            necess�rio verificar as permiss�es de acesso ao aqruivo de
	 *            fontes.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel configurar o estilo e falso
	 *         (false) caso contr�rio.
	 */
	public boolean setDefaultVisual(int rep, int red, int green, int blue,
			int style, int width, String fontName, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return setDefaultVisual(rep, red, green, blue, style, width, fontName,
				0, 0, 0, 0, 0, sessionId);
	}

	/**
	 * Recupera o visual padr�o, definido para um conjunto de objetos de uma
	 * representa��o a serem desenhados individualmente. � a segunda na ordem de
	 * prioridade sobre as demais defini��es de visual, sendo a SLD a primeira.
	 * Aten��o: Este visual padr�o � armazenado em mem�ria apenas e portanto
	 * vol�til.
	 * 
	 * @param rep
	 *            Representa��o geom�trica que receber� o novo visual comforme
	 *            defini��o TerraLib: c�lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: c�lulas
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Lista dos par�metros refer�ntes ao visual de uma representa��o
	 *         geom�trica, conforme defini��es em
	 *         {@link #setDefaultVisual(int, int, int, int, int, int, String, int, int, int, int, int, String)}
	 *         e
	 *         {@link #setDefaultVisual(int, int, int, int, int, int, String, String)}
	 */
	@SuppressWarnings("unchecked")
	public native Vector getDefaultVisual(int rep, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Recupera o visual padr�o, definido para uma representa��o do tema
	 * corrente. Aten��o: Este � o visual padr�o armazenado no banco e definido
	 * para um tema corrente.
	 * 
	 * @param rep
	 *            Representa��o geom�trica que receber� o novo visual comforme
	 *            defini��o TerraLib: c�lulas, poligonos, linhas, pontos e
	 *            texto.
	 * 
	 *            <pre>
	 * 1: poligonos
	 * 2: linhas
	 * 4: pontos
	 * 128: texto
	 * 256: c�lulas
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Lista dos par�metros refer�ntes ao visual de uma representa��o
	 *         geom�trica.
	 */
	@SuppressWarnings("unchecked")
	public native Vector getThemeVisual(int rep, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite configurar a apresenta��o de geometrias do tipo poligonos,
	 * baseado nos par�metros de visual dispon�veis na TerraLib. O par�metro
	 * persistence permite gravar as configura��es no banco de dados, alterando
	 * definitivamente o visual padr�o para desenho de poligonos.
	 * 
	 * @param styleId
	 *            Estilo para representa��o geom�trica.
	 * 
	 *            <pre>
	 * Poligono, observar o convencionado pelo TerraLib:
	 * 
	 * <b>Preenchimento c�lula ou poligono</b>: 0 = transparente, 1 = preenchimento opaco, 2 = hachura horizontal, 3 = hachura vertical,
	 * 4 = hachura diagonal inclina��o em 135�, 5 = hachura diagonal inclina��o em 45�,
	 * 6 = hachura horizontal e vertical, 7 = hachura horizontal e vertical inclinada em 45�
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria,
	 *            valores v�lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param transparency
	 *            Cor de preenchimento aceita valores no intervalo (0 - 100),
	 *            medida de porcentagem, para aplicar n�vel de transpar�ncia.
	 * @param contourStyleId
	 *            Considerar os estilos poss�veis para linha:
	 * 
	 *            <pre>
	 * <b>Contorno de poligonos</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = tra�o ponto, 4 = tra�o ponto ponto
	 * </pre>
	 * @param redContour
	 *            Componente vermelha da cor de contorno da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param greenContour
	 *            Componente verde da cor de contorno da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blueContour
	 *            Componente azul da cor de contorno da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param transparencyContour
	 *            Cor da linha de contorno aceita valores no intervalo (0 -
	 *            100), medida de porcentagem, para aplicar n�vel de
	 *            transpar�ncia.
	 * @param widthContour
	 *            Largura da linha de contorno do poligono.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso n�o armazena, mant�m na
	 *            mem�ria, e � perdido quando outro tema corrente � definido.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel configurar o estilo e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean setThemeVisualPolygon(int styleId, int red,
			int green, int blue, int transparency, int contourStyleId,
			int redContour, int greenContour, int blueContour,
			int transparencyContour, int widthContour, boolean persistence,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite configurar a apresenta��o de geometrias do tipo linha, baseado
	 * nos par�metros de visual dispon�veis na TerraLib. O par�metro persistence
	 * permite gravar as configura��es no banco de dados, alterando
	 * definitivamente o visual padr�o para desenho de linhas.
	 * 
	 * @param styleId
	 *            Estilo para representa��o geom�trica.
	 * 
	 *            <pre>
	 * Linha, observar o convencionado pelo TerraLib:
	 * <b>Linhas</b>: 0 = linha continua, 1 = tracejada, 2 = pontilhada, 3 = tra�o ponto, 4 = tra�o ponto ponto
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria,
	 *            valores v�lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param transparency
	 *            Cor de preenchimento aceita valores no intervalo (0 - 100),
	 *            medida de porcentagem, para aplicar n�vel de transpar�ncia.
	 * @param width
	 *            Largura da linha.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso n�o armazena, mant�m na
	 *            mem�ria, e � perdido quando outro tema corrente � definido.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel configurar o estilo e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean setThemeVisualLine(int styleId, int red, int green,
			int blue, int transparency, int width, boolean persistence,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite configurar a apresenta��o de geometrias do tipo ponto, baseado
	 * nos par�metros de visual dispon�veis na TerraLib. O par�metro persistence
	 * permite gravar as configura��es no banco de dados, alterando
	 * definitivamente o visual padr�o para desenho de pontos.
	 * 
	 * @param styleId
	 *            Estilo para representa��o geom�trica.
	 * 
	 *            <pre>
	 * Ponto, observar o convencionado pelo TerraLib:
	 * <b>Pontos</b>: 1 = estrela, 2 = circulo, 3 = X, 4 = quadrado, 5 = diamante, 6 = circulo vazado, 7 = quadrado vazado, 8 = diamente vazado
	 * </pre>
	 * @param red
	 *            Componente vermelha da cor de preenchimento da geometria,
	 *            valores v�lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento da geometria, valores
	 *            v�lidos no intervalo (0-255).
	 * @param size
	 *            Tamanho do ponto.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso n�o armazena, mant�m na
	 *            mem�ria, e � perdido quando outro tema corrente � definido.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel configurar o estilo e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean setThemeVisualPoint(int styleId, int red, int green,
			int blue, int size, boolean persistence, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * 
	 * Ver a assinatura simplificada de setThemeVisual, a qual pressup�e valores
	 * padr�o para os atributos que ainda n�o tem suporte habilitado na camada
	 * de desenho, definidos para uso futuro. Par�metros omitidos: boolean bold,
	 * boolean italic, double alignmentVert, double alignmentHoriz, int tabSize,
	 * int lineSpace
	 * ------------------------------------------------------------
	 * ----------------------------------- Permite configurar a apresenta��o de
	 * geometrias do tipo texto, baseado nos par�metros de visual dispon�veis na
	 * TerraLib. O par�metro persistence permite gravar as configura��es no
	 * banco de dados, alterando definitivamente o visual padr�o para desenho de
	 * texto. ATEN��O: Altera o visual de representa��es geom�tricas do tipo
	 * texto (criados pelo TerraView por exemplo), e n�o os textos desenhados
	 * din�micamente.
	 * 
	 * @param red
	 *            Componente vermelha da cor de preenchimento do caracter,
	 *            valores v�lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param redContour
	 *            Componente vermelha da cor de contorno do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param greenContour
	 *            Componente verde da cor de contorno do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blueContour
	 *            Componente azul da cor de contorno do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param size
	 *            Tamanho do texto.
	 * @param familyPath
	 *            Nome do arquivo de fonte e diret�rio onde o arquivo se
	 *            encontra, caminho completo. Pode ser necess�rio ajustar a
	 *            permiss�o de acesso a leitura do arquivo.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso n�o armazena, mant�m na
	 *            mem�ria, e � perdido quando outro tema corrente � definido.
	 * 
	 *            <pre>
	 * true = armazena no banco de dados.
	 * false = armazena apenas na mem�ria.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel configurar o estilo e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean setThemeVisualText(int red, int green, int blue,
			int redContour, int greenContour, int blueContour, int size,
			String familyPath, boolean bold, boolean italic,
			double alignmentVert, double alignmentHoriz, int tabSize,
			int lineSpace, boolean persistence, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite configurar a apresenta��o de geometrias do tipo texto, baseado
	 * nos par�metros de visual dispon�veis na TerraLib. O par�metro persistence
	 * permite gravar as configura��es no banco de dados, alterando
	 * definitivamente o visual padr�o para desenho de texto. ATEN��O: Altera o
	 * visual de representa��es geom�tricas do tipo texto (criados pelo
	 * TerraView por exemplo), e n�o os textos desenhados din�micamente.
	 * 
	 * @param red
	 *            Componente vermelha da cor de preenchimento do caracter,
	 *            valores v�lidos no intervalo (0-255).
	 * @param green
	 *            Componente verde da cor de preenchimento do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blue
	 *            Componente azul da cor de preenchimento do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param redContour
	 *            Componente vermelha da cor de contorno do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param greenContour
	 *            Componente verde da cor de contorno do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param blueContour
	 *            Componente azul da cor de contorno do caracter, valores
	 *            v�lidos no intervalo (0-255).
	 * @param size
	 *            Tamanho do texto.
	 * @param familyPath
	 *            Nome do arquivo de fonte e diret�rio onde o arquivo se
	 *            encontra, caminho completo. Pode ser necess�rio ajustar a
	 *            permiss�o de acesso a leitura do arquivo.
	 * @param persistence
	 *            Verdadeiro armazena no banco e falso n�o armazena, mant�m na
	 *            mem�ria, e � perdido quando outro tema corrente � definido.
	 * 
	 *            <pre>
	 * true = armazena no banco de dados.
	 * false = armazena apenas na mem�ria.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel configurar o estilo e falso
	 *         (false) caso contr�rio.
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
	 * identificadores (object_id). Somente a representa��o ativa (do tema
	 * corrente ou do tema de refer�ncia) ser�o desenhados. Este m�todo sempre
	 * considera o visual definido por setDefaultVisual.
	 * 
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, int, int, int, int, int, java.lang.String)">setDefaultVisual</a>
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, java.lang.String)">setDefaultVisual</a>
	 * @param objArray
	 *            Lista de identificadores de geometrias v�lidas para o tema
	 *            corrente ou de refer�ncia.
	 * @param themeType
	 *            Se vai operar sobre o tema corrente ou de refer�ncia:
	 * 
	 *            <pre>
	 * 0: tema corrente.
	 * 1: tema de refer�ncia.
	 * </pre>
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel desenhar as geometrias
	 *         solicitadas na lista e falso (false) caso contr�rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean drawSelectedObjects(Vector objArray, int themeType,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Desenha no canvas um conjunto de objetos indicados por uma lista de
	 * identificadores (object_id). Somente a representa��o ativa do tema
	 * corrente ser�o desenhados. Este m�todo � uma simplifica��o do
	 * {@link #drawSelectedObjects(Vector, int, String)} Este m�todo sempre
	 * considera o visual definido por setDefaultVisual.
	 * 
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, int, int, int, int, int, java.lang.String)">setDefaultVisual</a>
	 * @see <a
	 *      href="#setDefaultVisual(int, int, int, int, int, int, java.lang.String, java.lang.String)">setDefaultVisual</a>
	 * @param objArray
	 *            Lista de identificadores de geometrias v�lidas para o tema
	 *            corrente ou de refer�ncia.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return Verdadeiro (true) se foi poss�vel desenhar as geometrias
	 *         solicitadas na lista e falso (false) caso contr�rio.
	 */
	@SuppressWarnings("unchecked")
	public boolean drawSelectedObjects(Vector objArray, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return drawSelectedObjects(objArray, 0, sessionId);
	}

	/**
	 * Desenha uma legenda em um canvas auxiliar. Essa �rea ser� automaticamente
	 * apagada quando uma nova chamada a este m�todo for realizada. A imagem
	 * criada dever� ser recuperada atrav�s do m�todo getLegendImage.
	 * 
	 * @param legends
	 *            Lista de par�metros que representa uma legenda. Esta lista �
	 *            em formato String, e deve ser o retorno do m�todo
	 *            drawCurrentTheme.
	 * @param width
	 *            Largura da legenda gerada.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */

	public native void drawLegend(String legends, int width, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Desenha uma legenda em um canvas auxiliar. Essa �rea ser� automaticamente
	 * apagada quando uma nova chamada a este m�todo for realizada. A imagem
	 * criada dever� ser recuperada atrav�s do m�todo getLegendImage.
	 * 
	 * @param legends
	 *            Lista de par�metros que representa uma legenda. Esta lista �
	 *            em formato String, e deve ser o retorno do m�todo
	 *            drawCurrentTheme.
	 * @param width
	 *            Largura da legenda gerada.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 */

	@SuppressWarnings("unchecked")
	public native void drawLegends(Vector themesLegends,
			Vector<String> themeTitle, int width, int height, boolean fixed,
			boolean columns, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite alterar a cor de fundo padr�o da �rea de desenho, canvas. A cor
	 * de fundo padr�o � preto puro, ou seja os valores das componentes
	 * vermelho, verde, azul s�o zero (0,0,0).
	 * 
	 * @param r
	 *            Componente vermelha da cor de preenchimento do canvas, valores
	 *            v�lidos no intervalo (0-255).
	 * @param g
	 *            Componente verde da cor de preenchimento do canvas, valores
	 *            v�lidos no intervalo (0-255).
	 * @param b
	 *            Componente azul da cor de preenchimento do canvas, valores
	 *            v�lidos no intervalo (0-255).
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setCanvasBackgroundColor(int r, int g, int b,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite recuperar a escala atual. A unidade de medida � o metro, para um
	 * valor de pixel de (0,28 mm X 0,28 mm).
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return pixel Valor do pixel para a configura��o do box da �rea de
	 *         interesse com rela��o ao tamanho da imagem do dispositivo de
	 *         visualiza��o, tela, e o valor unit�rio do pixel adotado como um
	 *         valor m�dio padr�o de (0,28 mm X 0,28 mm).
	 */
	public native double getScale(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Habilita/Desabilita a detec��o de conflito ao imprimir r�tulos de texto
	 * sobre o desenho de mapa.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param conflictDetect
	 *            Verdadeiro habilita a detec��o de conflito e falso desabilita.
	 * 
	 *            <pre>
	 * true = Habilita.
	 * false = Desabilita.
	 * 
	 *            <pre>
	 * @param sessionId N�mero de controle de sess�o, geralmente gerado pelo servidor de aplica��o no
	 * momento da cria��o da sess�o do usu�rio, quando a primeira requisi��o � feita. Deve ser um identificador �nico.
	 * 
	 */
	public native void setConflictDetect(boolean conflictDetect,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Define a coluna da tabela de atributo que classificar�, por padr�o, em
	 * ordem decrescente de prioridade a impress�o de r�tulos de texto sobre o
	 * desenho de mapa. TODO: Permitir a altera��o da ordena��o dos r�tulos pelo
	 * atributo de prioridade.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param fieldName
	 *            Nome da coluna da tabela de atributos usada na ordena��o.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setPriorityField(String fieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define a toler�ncia para o calculo de colis�o entre r�tulos de texto
	 * sobre o desenho de mapa.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param numPixels
	 *            Valor da toler�ncia, em pixels, para o calculo de colis�o de
	 *            textos.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setMinCollisionTolerance(int numPixels, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define o tamanho m�nimo que uma geometria deve ter quando apresentada na
	 * �rea de desenho do dispositivo de sa�da, tela, para que esta receba um
	 * r�tulo din�mico.
	 * 
	 * @see <a
	 *      href="#getImageMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getImageMap</a>
	 * @param n
	 *            Valor da �rea, em pixels, para o filtro de geometrias que
	 *            assumem um tamanho reduzido na imagem de sa�da.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setGeneralizedPixels(int n, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define a coluna da tabela de atributo que ter� seus registros usados para
	 * a impress�o de r�tulos de texto sobre o desenho de mapa.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param fieldName
	 *            Nome da coluna da tabela de atributos usada como fonte dos
	 *            textos a serem desenhados.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setLabelField(String fieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define se o texto de r�tulo est�tico desenhado ser� desenhado com borda.
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setTextOutLineEnable(boolean turnon, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Define a cor da borda do texto de r�tulo est�tico.
	 * 
	 * @see <a
	 *      href="#drawLineAngleTextLabeling(java.lang.String)">drawLineAngleTextLabeling</a>
	 * @see <a
	 *      href="#drawHorizontalTextLabeling(java.lang.String)">drawHorizontalTextLabeling</a>
	 * @param r
	 *            Componente vermelha da cor de borda do texto, valores v�lidos
	 *            no intervalo (0-255).
	 * @param g
	 *            Componente verde da cor de borda do texto, valores v�lidos no
	 *            intervalo (0-255).
	 * @param b
	 *            Componente azul da cor de borda do texto, valores v�lidos no
	 *            intervalo (0-255).
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setImageMapProperties(String mapName, String mapId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Define se a tag do elemento HTML <b>map</b>, ser� fechada. Uma situa��o
	 * para o n�o fechamento seria incluir mais elementos <b>area</b> no corpo
	 * da tag antes de fechar a tag. <br/>
	 * &gt;map name="map" id="map"&lt;...&gt;/map&lt;
	 * 
	 * @see <a
	 *      href="#getImageMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getImageMap</a>
	 * @param hasToClose
	 *            Verdadeiro,fecha a tag, falso n�o fecha.
	 * 
	 *            <pre>
	 * true = fecha.
	 * false = n�o fecha.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 * &gt;area title="�rea:1234 m�"&lt;
	 * &gt;area title="�rea:5432 m�"&lt;
	 * &gt;area title="�rea:987 m�"&lt;
	 * Para gerar esta sa�da, use os seguintes valores:
	 * setAreaProperty("title", "�rea:%s m�", "coluna_area_lote", "12345")
	 * </pre>
	 * 
	 * @see <a
	 *      href="#getImageMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getImageMap</a>
	 * @param propertyName
	 *            Nome do atributo desejado para a tag <b>area</b>.
	 * @param propertyValue
	 *            Valor alfanumperico fixo para compor o valor do atributo.
	 * @param valueSrc
	 *            Nome da coluna da tabela de atributos que servir� de fonte
	 *            para compor o valor do atributo.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 */
	public native void setAreaProperty(String propertyName,
			String propertyValue, String valueSrc, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite gerar um mapa da imagem, conforme a defini��o de mapa de imagem
	 * para Web em HTML, usando a tag <b>map</b>. � poss�vel usar atributos de
	 * outras tabelas que n�o a de atributos est�tica, ver defini��o de tabelas
	 * est�ticas para layers TerraLib, para filtrar a sa�da recuperando apenas
	 * os r�tulos das geometrias que satisfa�am uma condi��o.
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
	 *            deseja incluir r�tulos din�micos.
	 * @param restrictionExpression
	 *            Clausula de filtro para restringir a sa�da.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Uma String contendo um c�digo HTML, construido com base na tag
	 *         <b>map</b> e sua subtag <b>area</b>.
	 */
	public native String getImageMap(String from, String linkAttr,
			String restrictionExpression, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Este m�todo permite recuperar um endere�o ou uma lista de endere�os que
	 * satisfa�am os filtros fornecidos. Ao finalizar o processamento de busca,
	 * um valor num�rico indicando o estado final da busca � gerado para
	 * informar o que foi achado e quais as possibilidades usadas durante a
	 * busca. O funcionamento deste m�todo pressup�e que a camada geogr�fica de
	 * informa��es sobre vias tenha sido preparada para responder a este tipo de
	 * pesquisa.
	 * 
	 * @see <a
	 *      href="#prepareGeocodingEnvironment(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)">prepareGeocodingEnvironment</a>
	 * @param locationName
	 *            Nome simples ou parte do nome simples da via de interesse,
	 *            este par�metro � obrigat�rio.
	 * @param locationNumber
	 *            N�mero do endere�o a ser localizado, este par�metro �
	 *            obrigat�rio.
	 * @param neighborhood
	 *            Nome do bairro, n�o obrigat�rio.
	 * @param zipCode
	 *            N�mero do CEP, n�o obrigat�rio.
	 * @param locationType
	 *            Classifica��o de tipo da via, n�o obrigat�rio.
	 * @param locationTitle
	 *            T�tulo da via, n�o obrigat�rio.
	 * @param locationPreposition
	 *            Preposi��o usada para formar o nome completo da via, n�o
	 *            obrigat�rio.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return O retorno deste m�todo � um Vector onde:
	 * 
	 *         <pre>
	 * [0] � um inteiro que representa o status da opera��o realizada pela TerraLib;
	 * <b>Valores poss�veis para o estado s�o:</b>
	 * 
	 * 0     -> Problema com a conex�o ao banco (ATEN��O: Este evento � lan�ado como Exception)
	 * 1     -> Endere�o n�o encontrado.
	 * 2     -> Endere�o vazio. (ATEN��O: Este evento � lan�ado como Exception)
	 * 3     -> Erro na SQL de busca. (ATEN��O: Este evento � lan�ado como Exception)
	 * 4     -> Endere�o �nico encontrado atrav�s do nome e n�mero.
	 * 5     -> V�rios endere�os encontrados atrav�s do nome e n�mero.
	 * 6     -> Endere�o �nico encontrado atrav�s do nome sem o n�mero.
	 * 7     -> V�rios endere�os encontrados atrav�s do nome sem o n�mero.
	 * 8     -> Endere�o encontrado por similaridade com o nome.
	 * 9     -> Endere�o encontrado usando o bairro ou o CEP.
	 * 10    -> Endere�o �nico encontrado atrav�s do nome e n�mero sem tipo.
	 * 11    -> V�rios endere�os encontrados atrav�s do nome e n�mero sem o tipo.
	 * 12    -> Endere�o �nico encontrado atrav�s do nome sem o n�mero e sem o tipo.
	 * 13    -> V�rios endere�os encontrados atrav�s do nome sem o n�mero e sem o tipo.
	 * 14    -> Endere�o �nico encontrado atrav�s do nome e n�mero sem o tipo e titulo.
	 * 15    -> V�rios endere�os encontrados atrav�s do nome e n�mero sem o tipo e titulo.
	 * 16    -> Endere�o �nico encontrado atrav�s do nome sem o n�mero, sem o tipo e sem o titulo.
	 * 17    -> V�rios endere�os encontrados atrav�s do nome sem o n�mero, sem o tipo e sem o titulo.
	 * 18    -> Endere�o �nico encontrado atrav�s do nome e n�mero sem o tipo, sem o titulo e sem a preposi��o.
	 * 19    -> V�rios endere�os encontrados atrav�s do nome e n�mero sem o tipo, sem o titulo e sem a preposi��o.
	 * 20    -> Endere�o �nico encontrado atrav�s do nome sem o n�mero, sem o tipo, sem o titulo e sem a preposi��o.
	 * 21    -> V�rios endere�os encontrados atrav�s do nome sem o n�mero, sem o tipo, sem o titulo e sem a preposi��o.
	 * 
	 * [n] demais indices s�o vetores, tipo Vector onde cada vetor � o conjunto de dados relacionados aos endere�os
	 * localizados a partir das informa��es de entrada fornecidas. S�o eles:
	 * <b>Valores das vari�veis de cada posi��o do array de descri��o de um endere�o:</b>
	 * 
	 * [0]  Tipo (java.lang.String). Valor do identificador �nico da geometria que representa o trecho da via onde o ponto foi encontrado.
	 * (Aten��o: Ap�s este processamento, como o ponto ainda n�o foi localizado, este valor n�o � preenchido).
	 * [1]  Tipo (java.lang.Integer). N�mero inicial esquerdo.
	 * [2]  Tipo (java.lang.Integer). N�mero final esquerdo.
	 * [3]  Tipo (java.lang.Integer). N�mero inicial direito.
	 * [4]  Tipo (java.lang.Integer). N�mero final esquerdo.
	 * [5]  Tipo (java.lang.String). A classifica��o do tipo da via (Av., Rua, R., Pr., ...)
	 * [6]  Tipo (java.lang.String). O t�tulo da via (Dr., Dra., Pref., ...)
	 * [7]  Tipo (java.lang.String). A preposi��o usada para formar o nome da via (Dos, Das, De, ...)
	 * [8]  Tipo (java.lang.String). O nome da via.
	 * [9]  Tipo (java.lang.String). O nome completo da via.
	 * [10] Tipo (java.lang.String). O nome do bairro do lado esquerdo da via.
	 * [11] Tipo (java.lang.String). O nome do bairro do lado direito da via.  
	 * [12] Tipo (java.lang.String). O CEP do lado esquerdo da via.
	 * [13] Tipo (java.lang.String). O CEP do lado direito da via.
	 * [14] Tipo (java.awt.geom.Point2D.Double) A coordenada do ponto que representa o endere�o pesquisado.
	 * (Aten��o: Ap�s este processamento o ponto ainda n�o foi localizado, apenas os dados do endere�o).
	 * [15] Tipo (java.lang.Boolean). Indica se o ponto localizado � uma coordenada v�lida.
	 * (Aten��o: Ap�s este processamento, como o ponto ainda n�o foi localizado, este valor n�o � v�lido).
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native Vector getAddressesDescription(String locationName,
			int locationNumber, String neighborhood, String zipCode,
			String locationType, String locationTitle,
			String locationPreposition, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * M�todo nativo que permite recuperar o ponto associado a um endere�o,
	 * definido pelo conjunto de informa��es do endere�o, encontrado pelo m�todo
	 * de busca de endere�os, getAddressesDescription, e representado no
	 * par�metro addressDescription. Este m�todo utiliza um algoritmo de
	 * interpola��o capaz de identificar um ponto correspondente a um endere�o,
	 * mesmo que o n�mero fornecido n�o exista, devendo apenas pertencer a um
	 * dos intervalos num�ricos, lado esquerdo ou direito da via, de algum dos
	 * trechos pertencentes � uma via identificada pelo nome.
	 * 
	 * @see <a
	 *      href="#getAddressesDescription(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)">getAddressesDescription</a>
	 * @param locationNumber
	 *            N�mero a ser localizado na via identificada pelos dados
	 *            fornecidos pelo par�metro addressDescription.
	 * @param addressDescription
	 *            Um vetor com os dados que permitem identificar de forma �nica
	 *            uma via.
	 * 
	 *            <pre>
	 * <b>Valores das vari�veis de cada posi��o do array de descri��o de um endere�o:</b>
	 * 
	 * [0]  Tipo (java.lang.String). Valor do identificador �nico da geometria que representa o trecho da via onde o ponto foi encontrado.
	 * (Aten��o: Este valor ser� preenchido, caso o ponto seja localizado).
	 * [1]  Tipo (java.lang.Integer). N�mero inicial esquerdo.
	 * [2]  Tipo (java.lang.Integer). N�mero final esquerdo.
	 * [3]  Tipo (java.lang.Integer). N�mero inicial direito.
	 * [4]  Tipo (java.lang.Integer). N�mero final esquerdo.
	 * [5]  Tipo (java.lang.String). A classificjava.lang.Stringo tipo da via (Av., Rua, R., Pr., ...)
	 * [6]  Tipo (java.lang.String). O t�tulo da via (Dr., Dra., Pref., ...)
	 * [7]  Tipo (java.lang.String). A preposi��o usada para formar o nome da via (Dos, Das, De, ...)
	 * [8]  Tipo (java.lang.String). O nome da via.
	 * [9]  Tipo (java.lang.String). O nome completo da via.
	 * [10] Tipo (java.lang.String). O nome do bairro do lado esquerdo da via.
	 * [11] Tipo (java.lang.String). O nome do bairro do lado direito da via.  
	 * [12] Tipo (java.lang.String). O CEP do lado esquerdo da via.
	 * [13] Tipo (java.lang.String). O CEP do lado direito da via.
	 * [14] Tipo (java.awt.geom.Point2D.Double) A coordenada do ponto que representa o endere�o pesquisado.
	 * (Aten��o: Este valor ser� preenchido, caso o ponto seja localizado).
	 * [15] Tipo (java.lang.Boolean). Indica se o ponto localizado � uma coordenada v�lida.
	 * (Aten��o: Este valor ser� preenchido, caso o ponto seja localizado).
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * 
	 * @return O retorno deste m�todo � um Vector, com a mesma estrutura do
	 *         par�metro addressDescription, contendo os valores devidamente
	 *         preenchidos para os itens:
	 * 
	 *         <pre>
	 * [0]  Tipo (java.lang.String). Valor do identificador �nico da geometria que representa o trecho da via onde o ponto foi encontrado.
	 * [14] Tipo (java.awt.geom.Point2D.Double) A coordenada do ponto que representa o endere�o pesquisado.
	 * (Aten��o: Este valor ser� preenchido, caso o ponto seja localizado).
	 * [15] Tipo (java.lang.Boolean). Indica se o ponto localizado � uma coordenada v�lida.
	 * (Aten��o: Este valor ser� preenchido, caso o ponto seja localizado).
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native Vector getPointCoordinate(int locationNumber,
			Vector<Object> addressDescription, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Desenha o ponto encontrado pelo m�todo getPointCoordinate na �rea de
	 * desenho, canvas, considerando que a coordenada do ponto est� na proje��o
	 * da vista corrente.
	 * 
	 * @param aPoint
	 *            O ponto a ser desenhado.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel desenhar o ponto e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean drawPointAddress(Point2D aPoint, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Acessa os metadados das tabelas de atributos relacionadas com uma camada
	 * de dados geogr�ficos, Layer, refer�nciado pelo tema corrente ou de
	 * refer�ncia. Ex: (Static attribute table name)-(Geo attribute
	 * link),(column1 name):(column1 type);(column2 name):(column2
	 * type)#(External attribute table name)-(External table attribute
	 * link)-(Static table attribute link),(column1 name):(column1 type)
	 * 
	 * @param themeType
	 *            Tipo de defini��o para o tema.
	 * 
	 *            <pre>
	 * <b>Dominio:</b>
	 * 0 = Tema corrente.
	 * 1 = Tema de refer�ncia.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Lista dos metadados, em formato padronizado como:
	 * 
	 *         <pre>
	 * (Static attribute table name)-(Geo attribute link),(column1 name):(column1 type);(column2 name):(column2 type)#(External attribute table name)-(External table attribute link)-(Static table attribute link),(column1 name):(column1 type);(column2 name):(column2 type)
	 * </pre>
	 */
	public native String getThemeMetadata(int themeType, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Destroe a inst�ncia do objeto nativo, TerraJava, associado a uma sess�o.
	 * Limpa a mem�ria.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel destruir o objeto associado a
	 *         uma sess�o e falso (false) caso contr�rio.
	 */
	public native boolean destroySession(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite definir o n�mero m�ximo de inst�ncias ativas do objeto nativo,
	 * TerraJava. 
	 * 
	 * @param maxInstances
	 *            N�mero m�ximo de conex�es.
	 */
	public native void setMaxInstances(int maxInstances)
			throws IllegalAccessException, InstantiationException;


	/**
	 * Permite gerar um mapa tem�tico, usando um atributo alfanum�rico para
	 * agrupar as geometrias, filtrando ou n�o a sa�da por um atributo
	 * alfanum�rico igual ou diferente do usado para agrupar geometrias. Os
	 * objeto geogr�ficos s�o representados por um tema, configurado como
	 * corrente. Adicionalmente, � gerada a legenda do agrupamento solicitado e
	 * retornada em formato de um vetor a ser passado numa lista junto com os
	 * outros temas desenhados para o m�todo drawLegends()
	 * 
	 * @param jfields
	 *            Nome da coluna usada para gerar o agrupamento
	 * @param jfrom
	 *            A tabela usada como tabela de atributos, a partir da qual foi
	 *            especificada a coluna no par�metro jfields.
	 * @param jlinkAttr
	 *            Nome da coluna que permite ligar os atributos com os objetos
	 *            geogr�ficos refer�nciados pelo tema corrente.
	 * @param jwhere
	 *            Clausula de filtro.
	 * @param jnumSlices
	 *            O n�mero de faixas para gerar os grupos de objetos
	 *            geogr�ficos.
	 * @param jgroupType
	 *            O tipo de algoritmo de classifica��o usado para agrupar os
	 *            objetos geogr�ficos.
	 * 
	 *            <pre>
	 * 0 = Passos Iguais
	 * 1 = Quantil
	 * 2 = Desvio Padr�o
	 * 3 = Valor �nico
	 * 
	 * <pre>
	 * @param r Incluir ou n�o a componente vermelha na legenda das classes geradas
	 * 
	 *            <pre>
	 * true = inclui
	 * false = n�o inclui
	 * </pre>
	 * @param g
	 *            Incluir ou n�o a componente verde na legenda das classes
	 *            geradas
	 * 
	 *            <pre>
	 * true = inclui
	 * false = n�o inclui
	 * </pre>
	 * @param b
	 *            Incluir ou n�o a componente azul na legenda das classes
	 *            geradas
	 * 
	 *            <pre>
	 * true = inclui
	 * false = n�o inclui
	 * </pre>
	 * @param jprec
	 *            N�mero de casas decimais consideradas usada na apresenta��o
	 *            dos intervalos de cada faixa gerada.
	 * @param jstdDev
	 *            O coeficiente de varia��o usado para permitir a compara��o
	 *            entre as faixas geradas quando o algoritmo de agrupamento
	 *            escolhido � o desvio padr�o.
	 * @param jsessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Um vetor de legendas que dever� ser usado para o desenho das
	 *         legendas no m�todo drawLegend
	 */
	@SuppressWarnings("unchecked")
	public native Vector drawGroupSql(int jtypeField, String jfields,
			String jfrom, String jlinkAttr, String jwhere, int jnumSlices,
			int jgroupType, boolean r, boolean g, boolean b, int jprec,
			int jstdDev, String jsessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Cria as tabelas de cole��o para um tema corrente. Ver modelo de dados
	 * TerraLib. Gera o ponto de refer�ncia onde um texto de r�tulo para uma
	 * geometria pode ser desenhado. Caso um identificador de um objeto
	 * geogr�fico seja fornecido, realiza a opera��o apenas para o objeto
	 * geogr�fico identificado.
	 * 
	 * @param objectId
	 *            Identificador de um objeto geogr�fico.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar esta opera��o e falso
	 *         (false) caso contr�rio.
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
	 *            Unidade de medida para a proje��o do dado.
	 * @param lat0
	 *            Latitude para a proje��o do dado.
	 * @param lon0
	 *            Longitude para a proje��o do dado.
	 * @param stlat1
	 *            Paralelo padr�o 1 para a proje��o do dado.
	 * @param stlat2
	 *            Paralelo padr�o 2 para a proje��o do dado.
	 * @param scale
	 *            Escala para a proje��o do dado.
	 * @param joffx
	 *            Offset X para a proje��o do dado.
	 * @param offy
	 *            Offset Y para a proje��o do dado.
	 * @param hemisphereNorth
	 *            O Hemisf�rio norte para a proje��o do dado.
	 * @param projectionName
	 *            O nome para a proje��o do dado.
	 * @param datum
	 *            A elipsoide para a proje��o do dado.
	 * @param filePath
	 *            Caminho completo e nome do arquivo onde est�o os daods
	 *            vetoriais.
	 * @param layerName
	 *            O nome da camada, layer, a ser criado no banco para
	 *            refer�nciar os dados importados.
	 * @param linkName
	 *            O nome da coluna de liga��o entre a tabela de atributos e a
	 *            tabela de geometrias que ser�o criadas para guardar os dados.
	 * @param attrTableName
	 *            O nome da tabela de atributos que ser� criada.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a importa��o e falso
	 *         (false) caso contr�rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean importShape(String filePath, String layerName,
			HashMap projectionMap, String linkName, String attrTableName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Retorna os nomes das coluna dispon�veis na tabela de atributo do arquivo
	 * shape.
	 * 
	 * @param filePath
	 *            Caminho completo e nome do arquivo onde est�o os dados
	 *            vetoriais.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Um vetor tipo Vector, com os nomes das colunas da tabela de
	 *         atributos do arquivo dbf, que comp�e o formato ShapeFile.
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
	 * L� configura��es de proje��o do shape.
	 * 
	 * @param filePath
	 *            Caminho completo e nome do arquivo onde est�o os dados
	 *            vetoriais.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Um vetor tipo Vector, com os dados sobre a proje��o do dado.
	 * 
	 *         <pre>
	 * <b>Ordem do retorno no vetor:</b>
	 * 
	 * [0]  String units Unidade de medida para a proje��o do dado.
	 * [1]  Double lat0 Latitude para a proje��o do dado.
	 * [2]  Double lon0 Longitude para a proje��o do dado.
	 * [3]  Double stlat1 Paralelo padr�o 1 para a proje��o do dado.
	 * [4]  Double stlat2 Paralelo padr�o 2 para a proje��o do dado.
	 * [5]  Double scale Escala para a proje��o do dado.
	 * [6]  Double offx Offset X para a proje��o do dado.
	 * [7]  Double offy Offset Y para a proje��o do dado.
	 * [8]  Boolean hemisphereNorth O Hemisf�rio norte para a proje��o do dado.
	 * [9]  String projectionName O nome para a proje��o do dado.
	 * [10] String datum A elipsoide para a proje��o do dado.
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native HashMap loadProjectionFromShape(String filePath,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Exporta para arquivo tipo ShapeFile a partir do theme corrente. Ainda �
	 * poss�vel passar um vetor com os nomes das coluna que deseja que sejam
	 * exportadas para o arquivo dbf. A opera��o de exporta��o leva em conta o
	 * box da �rea de interesse, e exporta apenas as geometrias que interceptam
	 * o box visivel.
	 * 
	 * @param filePath
	 *            Caminho completo onde ser�o gravados os arquivos ShapeFile,
	 *            exportados.
	 * @param attrVec
	 *            Vetor com os nomes das colunas da tabela de atributos da
	 *            camada de dados geogr�ficos que ser� exportada. Caso a lista
	 *            esteja vazia ser�o exportadas todas as colunas.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a exporta��o e falso
	 *         (false) caso contr�rio.
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
	 *            Nome do Layer de destino. O layer que ser� criado no banco.
	 * @param geomType
	 *            Tipo de geometria a ser importado, dentre os existentes para o
	 *            layer selecionado.
	 * 
	 *            <pre>
	 * 0 = Todas as representa��es
	 * 1 = Poligonos
	 * 2 = Linhas
	 * 4 = Pontos
	 * 128 = texto
	 * </pre>
	 * @param strDxfLayer
	 *            Um nome de layer dentre os dispon�veis no arquivo dxf.
	 * @param units
	 *            Unidade de medida para a proje��o do dado.
	 * @param lat0
	 *            Latitude para a proje��o do dado.
	 * @param lon0
	 *            Longitude para a proje��o do dado.
	 * @param stlat1
	 *            Paralelo padr�o 1 para a proje��o do dado.
	 * @param stlat2
	 *            Paralelo padr�o 2 para a proje��o do dado.
	 * @param scale
	 *            Escala para a proje��o do dado.
	 * @param offx
	 *            Offset X para a proje��o do dado.
	 * @param offy
	 *            Offset Y para a proje��o do dado.
	 * @param hemisphereNorth
	 *            O Hemisf�rio norte para a proje��o do dado.
	 * @param projectionName
	 *            O nome para a proje��o do dado.
	 * @param datum
	 *            A elipsoide para a proje��o do dado.
	 * @param attrList
	 *            Lista de colunas para compor a tabela de atributos.
	 * 
	 *            <pre>
	 * <b>Lista de colunas</b>
	 * [n] = Vector<Object>
	 * 
	 * <b>Descri��o de coluna</b>
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a exporta��o e falso
	 *         (false) caso contr�rio.
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
	 * 0 = Todas as representa��es
	 * 1 = Poligonos
	 * 2 = Linhas
	 * 4 = Pontos
	 * 128 = texto
	 * </pre>
	 * @param whereClause
	 *            Filtro para permitir a exporta��o de geometrias espec�ficas.
	 *            Usar o id de identifica��o de uma geometria.
	 * 
	 *            <pre>
	 * Exemplo: object_id in (1230, 2345, 4387)
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a exporta��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean exportDxf(String filePath, String layerName,
			int geomType, String whereClause, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Acesso aos tipos de geometrias existentes em um layer espec�fico,
	 * existente em um dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de origem.
	 * @param layerName
	 *            Nome do layer selecionado.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Tipo de geometrias dispon�veis em um layer.
	 * 
	 *         <pre>
	 * 1 = Poligono
	 * 2 = Linha
	 * 4 = Ponto
	 * 128 = Texto
	 * 256 = C�lula
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public native Vector dxfGeometryTypeFromLayer(String filePath,
			String layerName, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Acesso ao n�mero de layers existentes em um arquivo dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de origem.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return N�mero de layers encontrados no arquivo dxf.
	 */
	public native Integer dxfLayersCount(String filePath, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Acesso � lista de layers existentes em um arquivo dxf.
	 * 
	 * @param filePath
	 *            Nome e caminho do arquivo de origem.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Um vetor que representa a lista de nomes dos layer dispon�veis no
	 *         arquivo dxf.
	 */
	@SuppressWarnings("unchecked")
	public native Vector dxfListLayers(String filePath, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Recupera a lista de nomes das camadas de dados, layers, dispon�veis na
	 * base.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Um vetor que representa a lista de nomes dos layers dispon�veis
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
	 * Esse m�todo permite configurar os par�metros para opera��o de agrupamento
	 * personalizado. Criar as faixas desejadas, com uma cor de legenda para
	 * cada faixa, o intervalo de valores para cada faixa, uma descri��o para
	 * cada faixa. A quantidade de objetos geom�tricos que est�o em cada faixa �
	 * apenas para compor a informa��o da legenda de cada faixa, sendo opcional.
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
	 *            Lista de valores m�ximos de cada faixa.
	 * @param minList
	 *            Lista de valores minimos de cada faixa.
	 * @param descList
	 *            Lista de descri�ao de cada faixa.
	 * @param numObjList
	 *            Lista de quantidade de objetos de cada faixa.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 */
	@SuppressWarnings("unchecked")
	public native void setCustomGroupParameters(String legendTitle,
			Vector redList, Vector greenList, Vector blueList, Vector minList,
			Vector maxList, Vector descList, Vector numObjList, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo permite recuperar os par�metros usados em uma agrupamento
	 * anterior utilizando uma algoritmo pr�-formatado (Passos Iguais, Valor
	 * �nico , Desvio Padr�o..) para realizar um agrupamento personalizado
	 * usando as faixas geradas.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 *         description -> Descri��o da faixa color -> Cor da Faixa em RGB
	 * 
	 */
	@SuppressWarnings("unchecked")
	public native Vector getCustomGroupParameters(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo cria um novo layer com o nome, proje��o, lista de atributos e
	 * box informados.
	 * 
	 * @param layerName
	 *            Nome do layer a ser criado
	 * @param projectionHashMap
	 *            Proje��o do novo layer
	 * @param attList
	 *            Lista de atributos Para o par�metro attList considerar que �
	 *            um vector onde cada indice possui outro vector com os
	 *            metadados descritivos de cada coluna da tabela de atributos
	 *            obedecendo a seguinte convens�o: 0 => string (descri��o do
	 *            data type) (TeSTRING, TeREAL, etc...) 1 => int (comprimento do
	 *            campo) 2 => string (nome do campo) 3 => int (se campo � chave
	 *            prim�ria)
	 * @param x1
	 *            X da coordenada inferior esquerda do Box
	 * @param y1
	 *            Y da coordenada inferior esquerda do Box
	 * @param x2
	 *            X da coordenada superior direita do Box
	 * @param y2
	 *            Y da coordenada superior direita do Box
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	@SuppressWarnings("unchecked")
	public native boolean createLayer(String layerName,
			HashMap<String, Object> projectionHashMap, Vector attList,
			double x1, double y1, double x2, double y2, Vector geomRepVec, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo cria uma vista com o nome e username requisitado.
	 * 
	 * @param viewName
	 *            Nome da vista a ser criada
	 * @param userName
	 *            Nome do usu�rio a ser associado como dono da nova vista.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean createView(String viewName, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * M�todo para atualiza��o de um layer.
	 * 
	 * @param layerId
	 *            id do layer que ser� atualizado.
	 * @param newLayerName
	 *            Novo nome do layer.
	 * @param projectionHashMap
	 *            Nova Proje��o do layer
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean updateLayer(int layerId, String newLayerName,
			HashMap<String, Object> projectionHashMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo renomeia a vista corrente com o novo nome solicitado.
	 * 
	 * @param viewNewName
	 *            Novo nome da vista.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean updateView(String viewNewName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo cria um tema a partir de uma camada de dados, layer, com o
	 * nome especificado. O tema associa um estilo de visualiza��o aos dados
	 * brutos da camada de dados, layer. Na cria��o um tema recebe um visual
	 * padr�o para todas as geometrias que este mapeia. � poss�vel denifir a
	 * qual tema grupo o novo tema ser� associado como filho.
	 * 
	 * @param themeName
	 *            Nome do theme a ser criado
	 * @param layerName
	 *            Nome do layer a partir do qual o tema ser� criado.
	 * @param parentId
	 *            Id do tema grupo pai na qual o tema ser� associado como filho.
	 *            Caso n�o associar a nenhum grupo, passar o valor como 0.
	 * @param restriction
	 *            Clausula SQL que ser� usada para restringir com os atributos
	 *            os objetos geogr�ficos do layer no novo tema criado.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
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
	 * Esse m�todo modifica o theme corrente da vista corrente. Atualiza o nome
	 * do tema e o id do tema grupo parente
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param themeNewName
	 *            Nome do tema a ser alterado, tema alvo.
	 * @param parentId
	 *            Id do tema grupo parente a qual este tema deve ser adicionado
	 *            como filho, se 0 ou o seu mesmo id ele ficar� na raiz da
	 *            hierarquia.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean updateTheme(String themeNewName, int parentId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	public boolean updateTheme(String themeNewName, String sessionId)
			throws IllegalAccessException, InstantiationException {
		return updateTheme(themeNewName, 0, sessionId);
	}

	/**
	 * Esse m�todo renomeia a vista corrente.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @param viewNewName
	 *            Novo nome da vista.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean updateViewName(String viewNewName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo remove um layer do banco de dados, dado um id.
	 * 
	 * @param layerId
	 *            id do layer que ser� removido.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean deleteLayer(int layerId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo remove a vista corrente.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean removeView(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo remove o tema corrente.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean removeTheme(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo retorna os metadados (Tabelas de atributos, atributos,
	 * representa��es e proje��o) dos layers dispon�veis no database conectado
	 * 
	 * @param forceReload
	 *            For�ar recarregamento dos layers do banco de dados
	 * @param loadAttrList
	 *            Carregar lista de tabelas de atributos ou n�o
	 * @param sessionId
	 *            N�mero de controle de sess�o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public native Vector getLayerSet(boolean forceRealod, boolean loadAttrList,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Esse m�todo retorna os metadados (Tabelas de atributos, atributos) do
	 * layer requisitado.
	 * 
	 * @param layerId
	 *            Identificador do layer para carregar os atributos
	 * @param sessionId
	 *            N�mero de controle de sess�o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public native Vector getLayerAttrTables(int layerId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Este m�todo retorna o metadado das views, temas, layers e atributos do
	 * banco de dados.
	 * 
	 * @param dbUsername
	 *            Nome de um usu�rio do banco de dados. Lembre-se, as vistas s�o
	 *            associadas aos usu�rios.
	 * @param forceReload
	 *            For�a re-leitura dos metadados do banco. Valor padr�o = false.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna todos os metadados das vistas, temas, layers e atributos
	 *         em um vetor tipo Vector&gt;HashMap&lt;
	 */
	@SuppressWarnings("unchecked")
	public native Vector getViewSet(String dbUsername, boolean forceReload,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Este m�todo retorna o metadado das views, temas, layers e atributos do
	 * banco de dados respeitando a forma de hierarquia da arvore de temas e
	 * temas de groupo.
	 * 
	 * @param forceReload
	 *            For�a re-leitura dos metadados do banco. Valor padr�o = false.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna todos os metadados das vistas, temas, layers e atributos
	 *         em um vetor tipo Vector&gt;HashMap&lt;
	 */
	@SuppressWarnings("unchecked")
	public native Vector getViewSetTree(boolean forceReload, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Este m�todo retorna os metadados configurados para permitir as opera��es
	 * de geocodifica��o para um layer.
	 * 
	 * @param layerId
	 *            Id do layer requisitado.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Hashmap O mapa descrito abaixo.
	 * 
	 *         <pre>
	 * <b>Um Hashmap de retorno apresenta as seguintes chaves com seus respectivos valores:</b>
	 * locationCompleteName Nome da coluna da tabela de atributos que armazena o nome completo da via.
	 * tableId Identificador da tabela de atributos usada na prepara��o para o processamento de opera��es de geocodifica��o.
	 * initialLeftNumber Nome da coluna da tabela de atributos que armazena o n�mero inicial esquerdo da via.
	 * initialRightNumber Nome da coluna da tabela de atributos que armazena o n�mero inicial direito da via.
	 * finalLeftNumber Nome da coluna da tabela de atributos que armazena o n�mero final esquerdo da via.
	 * finalRightNumber Nome da coluna da tabela de atributos que armazena o n�mero final direito da via.
	 * locationType Nome da coluna da tabela de atributos que armazena o tipo da via.
	 * locationTitle Nome da coluna da tabela de atributos que armazena o titulo da via.
	 * locationPreposition Nome da coluna da tabela de atributos que armazena a preposi��o usada para formar o nome da via.
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
	 * Esse m�todo prepara o ambiente de geocodifica��o passando os par�metros
	 * necess�rio a serem cadastrados para realizar uma busca de endere�o,
	 * geocodifica��o.
	 * 
	 * @param layerId
	 *            Identificador do layer
	 * @param initialLeftNumber
	 *            Nome da coluna da tabela de atributos que armazena o n�mero
	 *            esquerdo inicial
	 * @param initialRightNumber
	 *            Nome da coluna da tabela de atributos que armazena o n�mero
	 *            direito inicial
	 * @param finalLeftNumber
	 *            Nome da coluna da tabela de atributos que armazena o n�mero
	 *            esquerdo final
	 * @param finalRightNumber
	 *            Nome da coluna da tabela de atributos que armazena o n�mero
	 *            direito final
	 * @param locationType
	 *            Nome da coluna da tabela de atributos que armazena o tipo da
	 *            via.
	 * @param locationTitle
	 *            Nome da coluna da tabela de atributos que armazena o titulo da
	 *            via.
	 * @param locationPreposition
	 *            Nome da coluna da tabela de atributos que armazena a
	 *            preposi��o usada para compor o nome completo da via.
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
	 *            Nome da coluna da tabela de atributos que armazena o n�mero do
	 *            CEP do lado esquerdo da via.
	 * @param rightZipCode
	 *            Nome da coluna da tabela de atributos que armazena o n�mero do
	 *            CEP do lado direito da via.
	 * @param locationCompleteName
	 *            Nome da coluna da tabela de atributos que armazena o nome
	 *            completo da via, caso exista.
	 * @param nameColumnCompleteName
	 *            Nome da coluna a ser criada para o nome completo da via, caso
	 *            n�o exista ou queira criar uma coluna nova. Esta opera��o
	 *            executa um update na tabela de atributos, e pode ser demorada
	 *            dependendo do n�mero de registros existente nesta tabela.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
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
	 * Esse m�todo permite definir se na pr�xima requisi��o de desenho a
	 * representa��o de texto ser� desenhada. Este texto n�o deve ser confundido
	 * com o os r�tulos est�ticos ou din�micos. Trata-se dos textos definidos
	 * como geometrias pelo TerraView.
	 * 
	 * @param drawText
	 *            Verdadeiro ou falso.
	 * 
	 *            <pre>
	 * true = desenha
	 * false = n�o desenha.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 */
	public native void setDrawTextRepresentation(boolean drawText,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Esse m�todo permite a configura��o, persistida ou n�o dos limites
	 * inferior e superior de escala de visualiza��o.
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
	 * false = n�o persiste.
	 * </pre>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean setThemeScaleLimit(double minScale, double maxScale,
			boolean persistScaleLimit, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo permite a recupera��o dos limites superior e inferior de
	 * escala de visualiza��o definidida para o tema corrente.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return HashMap com as keys (maxScale, minScale)
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getThemeScaleLimit(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo permite inser��o de geometrias em um layer. Configurar a
	 * vista corrente e um tema, � pr�-requisito.
	 * 
	 * @param representation
	 *            Tipo de representa��o vetorial:
	 * 
	 *            <pre>
	 * 1 = poligono
	 * 2 = linha
	 * 4 = ponto
	 * </pre>
	 * @param verticeList
	 *            Lista de v�rtices.
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
	 *            Par�metros da proje��o em um HashMap.
	 * 
	 *            <pre>
	 * <b>Lista padronizada de par�metros (exemplo de uso):</b>
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean addGeometry(int representation,
			Vector<Object> verticeList, Vector<Object> attrList,
			String layerName, HashMap<String, Object> projectionHashMap,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Este m�todo permite e reproje��o de coordenadas geogr�ficas da proje��o
	 * do dado para uma proje��o especifica.
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
	 *            Hash map com os par�metros de proje��o atual das coordenadas
	 * @param destinationProjectionMap
	 *            Hash map com os parametros de proje��o desejada
	 * 
	 *            <pre>
	 * <b>Estrutura do hashmap para proje��es:</b>
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Lista das coordenadas de entrada remapeadas para a proje��o de
	 *         sa�da especificada pelo par�metro destinationProjectionMap. Segue
	 *         a mesma estrutura do vetor de entrada, definido pelo par�metro
	 *         coordsList.
	 */
	public native Vector<Object> remapCoordinates(Vector<Object> coordsList,
			HashMap<String, Object> dataProjectionMap,
			HashMap<String, Object> destinationProjectionMap, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo permite a uni�o do box dos temas especificados pela lista de
	 * Ids de temas.
	 * 
	 * @param themesId
	 *            Lista de ids de temas, formato Vector.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 * Permite definir a atua��o do controle de escala sobre os temas. O
	 * controle de escala permite que as geometrias representadas por um tema
	 * sejam desenhadas em intervalos de escalas pr� definidos, um intervalo por
	 * tema, melhorando o desempenho e a apresenta��o de camadas de dados que
	 * possuem n�veis de detalhamento melhor definidos em escalas diferentes.
	 * Aten��o: Atua apenas nas camadas para as quais existe defini��o de
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 */
	public native void setAutomaticScaleControlEnable(
			boolean scaleControlEnabled, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria uma vista em mem�ria a partir de um documento SLD xml.
	 * 
	 * @param path
	 *            O caminho do documento SLD.
	 * @param userName
	 *            Nome do usu�rio a ser associado como dono da nova vista.
	 * @param viewName
	 *            Nome para a vista que ser� criada. Caso vazio, utiliza o nome
	 *            definido no documento SLD.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna os temas inv�lidos, definidos em tempo de cria��o. O
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 */
	public native void saveCurrentView(String path, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria uma vista em mem�ria com temas previamente armazenados no banco.
	 * 
	 * @param viewName
	 *            Nome para a nova vista.
	 * @param themeIds
	 *            Identificadores dos temas previamente armazenados no banco e
	 *            que ser�o incoporados a nova vista.
	 * @param projectionHashMap
	 *            Proje��o da nova vista.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true se a vista foi criada com sucesso, falso caso
	 *         contr�rio.
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 *            Proje��o do tema.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true se o tema foi criado com sucesso, falso caso
	 *         contr�rio.
	 */
	public native boolean createFileTheme(String themeName, String path,
			int parentId, HashMap<String, Object> projectionHashMap,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Remove um tema da mem�ria.
	 * 
	 * @param themeId
	 *            Identificador do tema que ser� removido.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true se o tema foi removido da vista com sucesso, falso
	 *         caso contr�rio.
	 */
	public native boolean removeThemeMem(int themeId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Renomeia em mem�ria o tema corrente.
	 * 
	 * @param newName
	 *            Novo nome para o tema corrente
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true se o tema corrente foi renomeado com sucesso, falso
	 *         caso contr�rio.
	 */
	public native boolean renameThemeMem(String newName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Esse m�todo modifica o file theme corrente da vista corrente. Atualiza o
	 * nome do tema e o id do tema grupo parente
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param newName
	 *            Nome do tema a ser alterado, tema alvo.
	 * @param parentId
	 *            Id do tema grupo parente a qual este tema deve ser adicionado
	 *            como filho, se 0 ou o seu mesmo id ele ficar� na raiz da
	 *            hierarquia.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Verdadeiro (true) se foi poss�vel realizar a opera��o e falso
	 *         (false) caso contr�rio.
	 */
	public native boolean updateFileTheme(String newName, int parentId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Define o caminho para o dado de um tema do tipo arquivo.
	 * 
	 * @param themeName
	 *            Nome do tema arquivo que ser� atualizado.
	 * @param path
	 *            Caminho para o dado do tema. Ex. ../../brasil.shp
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 */
	public native void setFileThemePath(String themeName, String path,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	public native byte[] drawThemeLegend(String title, int width, int height,
			boolean fixed, boolean columns, int legendImageType,
			boolean legendOpaque, int legendImageQuality, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Salva um vista previamente criada em mem�ria para o banco de dados.
	 * 
	 * @param viewName
	 *            Nome da vista que ser� armazenada no banco de dados.
	 * @param userName
	 *            Nome do usu�rio propriet�rio da vista.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true se a vista foi armazenada com sucesso, falso caso
	 *         contr�rio.
	 */
	public native boolean saveView2DB(String viewName, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Remove da mem�ria uma ista.
	 * 
	 * @param viewName
	 *            Nome da vista que ser� removida da mem�ria.
	 * @param userName
	 *            Nome do usu�rio propriet�rio da vista.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true se a vista foi removida com sucesso, falso caso
	 *         contr�rio.
	 */
	public native boolean removeViewMem(String viewName, String userName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Obt�m o nome da vista definido em um documento SLD xml.
	 * 
	 * @param path
	 *            Caminho para o documento SLD xml;
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna o nome da vista.
	 */
	public native String getViewNameFromSLD(String path, String sessionId)
			throws IllegalAccessException, InstantiationException;

	public native boolean saveFileTheme2DB(String name, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * A fun��o permite a defini��o dos temas visiveis e n�o visiveis para a
	 * vista corrente. A visibilidade dos temas pode ser definida tanto no banco
	 * de dados como somente na mem�ria da sess�o atual, passando o parametro
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
	 * themeVisMap2.put("visibility", false); <-- Tema n�o visivel   
	 * themesVect.add(themesVisMap1);
	 * themesVect.add(themesVisMap2);
	 * </pre>
	 * 
	 * @param persist
	 *            Deseja persistir no banco de dados ou manter simplesmente em
	 *            mem�ria
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Sucesso ao gravar visibilidade dos temas ou n�o.
	 */
	@SuppressWarnings("unchecked")
	public native boolean setThemesVisibility(Vector<HashMap> themesVec,
			boolean persist, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Fun��o permite a cria��o de grupo de temas direto na vista ou dentro de
	 * outro grupo de temas. � necess�rio setar a vista corrente onde ser�
	 * inserido o tema.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @param themeGroupName
	 *            Nome do tema grupo que ser� criado.
	 * @param parentId
	 *            Id do tema grupo parente onde ser� criado o tema. Caso 0 ser�
	 *            criado na vista.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return
	 */
	public native boolean createThemeGroup(String themeGroupName, int parentId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Fun��o permite apagar um tema grupo e todos seus filhos. � necess�rio
	 * setar a vista corrente onde ser� inserido o tema.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @param themeGroupId
	 *            Id do tema grupo quer ser� apagado
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return
	 */
	public native boolean deleteThemeGroup(int themeGroupId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Fun��o permite alterar o nome de um tema grupo e alterar sua posi��o na
	 * �rvore de temas, passando como parametro o id do grupo na qual o tema
	 * pertencer�, caso 0 ser� na vista.
	 * 
	 * @param themeGroupId
	 *            Id do tema grupo quer ser� alterado
	 * @param themeGroupNewName
	 *            Novo nome para o tema
	 * @param parentId
	 *            Id do grupo para onde o tema ser� filho
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return
	 */
	public native boolean updateThemeGroup(int themeGroupId,
			String themeGroupNewName, int parentId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Fun��o permite salvar a ordem/prioridade no carregamento e no desenho dos
	 * temas e temas grupos.
	 * 
	 * @param themesList
	 *            Lista de Maps de temas com o id do tema e o n�mero da
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
	 * @param sessionId N�mero de controle de sess�o, geralmente gerado pelo servidor de aplica��o no
	 * momento da cria��o da sess�o do usu�rio, quando a primeira requisi��o � feita. Deve ser um identificador �nico.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public native boolean saveThemesPriorities(Vector themesList,
			boolean persist, String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Fun��o retorna lista de Id's dos temas que podem ser plotados na BBox e
	 * escala atuais.
	 * 
	 * @see <a
	 *      href="#setCurrentView(java.lang.String, java.lang.String, java.lang.String)">setCurrentView</a>
	 * @see <a
	 *      href="#setWorld(double, double, double, double, int, int, java.lang.String)">setWorld</a>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Lista de id's dos temas visiveis
	 */
	public native Vector<Integer> getThemesToPlot(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Fun��o permite a cria��o de uma tabela tempor�ria com uma coluna e com os
	 * valores requisitados. Utilizada quando existem muitos registros para
	 * executar numa SQL inn (na qual possui limite de caracteres), podendo
	 * assim fazer um join com a tabela tempor�ria ao inv�s de usar a clausula.
	 * 
	 * @param tableName
	 *            Nome da tabela a ser criada
	 * @param columnName
	 *            Nome da coluna a ser criada na tabela tempor�ria para os
	 *            valores
	 * @param valuesVector
	 *            Lista de valores para serem adicionados na tabela.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso tenha criado a tabela.
	 */
	public native boolean createTemporaryTableWithValues(String tableName,
			String columnName, Vector<String> valuesVector, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Deleta tabela tempor�ria que foi previamente criada.
	 * 
	 * @param tableName
	 *            Nome da tabela existente.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso tenha deletado a tabela.
	 */
	public native boolean deleteTemporaryTable(String tableName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	public native void setWorkProjection(HashMap<String, Object> projection,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Fun��o permite recuperar todas as geometrias do tema corrente no formato
	 * WKB
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Geometrias no formato WKB
	 */
	public native byte[] getThemeGeometriesOnWKB(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Fun��o permite recuperar geometrias com os identificadores dos objetos
	 * solicitados do tema corrente no formato WKT
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param oids
	 *            Lista de identificadores dos objetos.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Geometrias no formato WKT
	 */
	public native String getGeometriesByOidOnWKT(Vector<String> oids,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Fun��o permite recuperar geometrias com os identificadores das geometrias
	 * solicitadas do tema corrente no formato WKT
	 * 
	 * @see <a
	 *      href="#setTheme(java.lang.String, int, java.lang.String)">setTheme</a>
	 * @param oids
	 *            Lista de identificadores das geometrias.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Geometrias no formato WKT
	 */
	public native String getGeometriesByGeomIdOnWKT(Vector<String> geomIds,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Fun��o permite recuperar todas as geometrias do tema corrente na seguinte
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Geometrias do tema
	 */
	@SuppressWarnings("unchecked")
	public native Vector getThemeGeometries(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Fun��o permite recuperar geometrias com os Objects Ids solicitados do
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Geometrias do tema
	 */
	@SuppressWarnings("unchecked")
	public native Vector getGeometriesByOid(Vector<String> Oids,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Realiza a interse��o entre o tema corrente e o tema de refer�ncia.
	 * 
	 * @param layerName
	 *            Nome do novo layer que ser� gerado pela opera��o.
	 * @param useThemeOverlayAttr
	 *            Define se os atributos do tema de refer�ncia devem ser
	 *            inclu�dos no novo layer.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso a opera��o tenha sido realizada corretamente,
	 *         false caso contr�rio.
	 */
	public native boolean intersection(String layerName,
			boolean useThemeOverlayAttr, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a interse��o entre o tema corrente (gerado a partir de uma layer
	 * raster) e o tema de refer�ncia.
	 * 
	 * @param layerName
	 *            Nome do novo layer que ser� gerado pela opera��o.
	 * @param backValue
	 *            Valor usado para background (dummy)
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso a opera��o tenha sido realizada corretamente,
	 *         false caso contr�rio.
	 */
	public native boolean intersectionRaster(String layerName,
			double backValue, String sessiondId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Realiza a diferen�a entre o tema corrente e o tema de refer�ncia nesta
	 * ordem: (tema corrente - tema refer�ncia).
	 * 
	 * @param layerName
	 *            Nome do novo layer que ser� gerado pela opera��o.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso a opera��o tenha sido realizada corretamente,
	 *         false caso contr�rio.
	 */
	public native boolean difference(String layerName, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a interse��o entre o tema corrente e uma lista de geometrias em
	 * mem�ria. A lista em mem�ria � preenchida utilizando o m�todo
	 * locateObject, definindo o par�metro stroreGeom = true
	 * 
	 * @param layerName
	 *            Nome do novo layer que ser� gerado pela opera��o.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso a opera��o tenha sido realizada corretamente,
	 *         false caso contr�rio.
	 */
	public native boolean mask(String layerName, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a interse��o entre o tema corrente (gerado a partir de uma layer
	 * raster) e uma lista de geometrias em mem�ria. A lista em mem�ria �
	 * preenchida utilizando o m�todo locateObject, definindo o par�metro
	 * stroreGeom = true
	 * 
	 * @param layerName
	 *            Nome do novo layer que ser� gerado pela opera��o.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso a opera��o tenha sido realizada corretamente,
	 *         false caso contr�rio.
	 */
	public native boolean maskRaster(String layerName, double backValue,
			String sessiondId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Realiza a diferen�a entre o tema corrente e uma lista de geometrias em
	 * mem�ria. A lista em mem�ria � preenchida utilizando o m�todo
	 * locateObject, definindo o par�metro stroreGeom = true
	 * 
	 * @param layerName
	 *            Nome do novo layer que ser� gerado pela opera��o.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso a opera��o tenha sido realizada corretamente,
	 *         false caso contr�rio.
	 */
	public native boolean differenceM(String layerName, String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Limpa a lista de geometrias em mem�ria.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 */
	public native void clearGeomList(String sessiondId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Realiza a agrega��o no tema corrente a partir da lista de atributos
	 * selecionados.
	 * 
	 * @param layerName
	 *            Nome do novo layer que ser� gerado pela opera��o.
	 * @param agregAttrs
	 *            Atributos que ser�o a base da agrega��o.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna true caso a opera��o tenha sido realizada corretamente,
	 *         false caso contr�rio.
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
	 * Permite a associa��o de informa��es (nome, autor, fonte, qualidade,
	 * descri��o, data de cria��o, hora de cria��o) a um layer j� existente na
	 * base de dados.
	 * 
	 * @param layerId
	 *            Idenficador do layer criado previamente
	 * @param name
	 *            Nome do layer (Apelido)
	 * @param author
	 *            Autor do layer
	 * @param source
	 *            Fonte da informa��o do layer
	 * @param quality
	 *            Qualidade do layer
	 * @param description
	 *            Descri��o
	 * @param date
	 *            Data de cria��o
	 * @param hour
	 *            Hora de cria��o
	 * @param transf
	 *            ?
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorn true caso sucesso na associa��o.
	 */
	public native boolean setLayerMetadata(int layerId, String name,
			String author, String source, String quality, String description,
			String date, String hour, boolean transf, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a recupera��o de informa��es associadas a um layer existente na
	 * base de dados.
	 * 
	 * @param layerId
	 *            Idenficador do layer criado previamente
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return HashMap com as seguintes keys: layerId; name; author; source;
	 *         quality; description; date; hour; tranf;
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getLayerMetadata(int layerId, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a recupera��o de informa��es associadas a todos os layers
	 * existentes na base de dados.
	 * 
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Vector com HashMaps com as seguintes keys: layerId; name; author;
	 *         source; quality; description; date; hour; tranf;
	 */
	@SuppressWarnings("unchecked")
	public native HashMap getLayersMetadata(String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite e exclus�o de todos os objetos (geometrias e attributos) do layer
	 * solicitado mantendo as outras configura��es do layer.
	 * 
	 * @param layerId
	 *            Identificador do layer a ser excluidas as geometrias.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Retorna True caso tenha sucesso na remo��o.
	 */
	public native boolean deleteAllObjectsFromLayer(int layerId,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite usando como refer�ncia um BOX definido previamente setar uma
	 * escala de visualiza��o.
	 * 
	 * @param scale
	 *            Escala a setar
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return HashMap com o novo BOX chaves(x1,y1,x2,y2)
	 */
	@SuppressWarnings("unchecked")
	public native HashMap setScale(double scale, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a importa��o de imagens rasters dentro de uma mesmo layer
	 * (Mosaico)
	 * 
	 * @param layerName
	 *            Nome do Layer que ser� criado
	 * @param filePathList
	 *            Vetor de String com os caminhos dos rasters
	 * @param multiRes
	 *            Inteiro para a op��o de multiresolu��o. Valores menores ou
	 *            igual a um definem que n�o haver� multiresolu��o, e valores
	 *            acima definem que haver�, e quantas resolu��es ser�o.
	 * @param dummy
	 *            , cor de pixel a ser ignorada durante o desenho da imagem
	 *            (0-255)
	 * @param projectionMap
	 *            , proje��o para criar o layer, caso a proje��o do raster seja
	 *            diferente, o raster ser� reprojetado. Chaves do HashMap:
	 *            projDatum (String), projUnits (String), projName (String),
	 *            projLat0 (Double), projLon0 (Double), projStLat1 (Double),
	 *            projStLat2 (Double), projScale (Double), projOffx (Double),
	 *            projOffy (Double), projNorthHemisphere (Boolean)
	 * @param logPath
	 *            Local onde os logs ser�o armazenados.
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return boolean Resultado da opera��o
	 */
	@SuppressWarnings("unchecked")
	public native boolean importRasterList(String layerName,
			Vector<String> filePathList, int multiRes, int dummy,
			HashMap projectionMap, String logPath, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Recupera as proje��es de uma lista de rasters
	 * 
	 * @param filePathList
	 *            Vetor de String com os caminhos dos rasters
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Lista de HashMaps de proje��es - Chaves do HashMap: projDatum (String), projUnits
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
	 * Cria um campo relacionado a �rea na tabela do layer caso o mesmo n�o
	 * exista. Calcula os valores das �reas das geometrias pertencentes a este
	 * layer.
	 * 
	 * @param layerId
	 *            Idenficador do layer que ser� usado para a opera��o
	 * @param areaFieldName
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return boolean Resultado da opera��o
	 */
	public native boolean createOrReplaceAreaField(int layerId,
			String areaFieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria um campo relacionado a per�metro na tabela do layer caso o mesmo n�o
	 * exista. Calcula os valores dos per�metros das geometrias pertencentes a
	 * este layer.
	 * 
	 * @param layerId
	 *            Idenficador do layer que ser� usado para a opera��o
	 * @param perimeterFieldName
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return boolean Resultado da opera��o
	 */
	public native boolean createOrReplacePerimeterField(int layerId,
			String perimeterFieldName, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Cria 2 campos relacionados a centr�ide na tabela do layer caso o mesmo
	 * n�o exista. Calcula os valores das centr�ides das geometrias pertencentes
	 * a este layer.
	 * 
	 * @param layerId
	 *            Idenficador do layer que ser� usado para a opera��o
	 * @param centroidXFieldName
	 * @param centroidYFieldName
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return boolean Resultado da opera��o
	 */
	public native boolean createOrReplaceCentroidField(int layerId,
			String centroidXFieldName, String centroidYFieldName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite a cria��o de uma nova coluna na tabela de atributos do layer
	 * solicitado com os parametros requisitados.
	 * 
	 * @param layerId
	 *            Idenficador do layer que ser� usado para a opera��o
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Resultado da opera��o
	 */
	public native boolean createAttributeColumn(int layerId, String columnName,
			String columnType, int columnSize, String sessionId)
			throws IllegalAccessException, InstantiationException;

	/**
	 * Permite a exclus�o de uma coluna na tabela de atributos.
	 * 
	 * @param layerId
	 *            Idenficador do layer que ser� usado para a opera��o
	 * @param columnName
	 *            Nome da coluna a ser exclu�da
	 * @param sessionId
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Resultado da opera��o
	 */
	public native boolean deleteAttributeColumn(int layerId, String columnName,
			String sessionId) throws IllegalAccessException,
			InstantiationException;

	/**
	 * Permite a atualiza��o de uma coluna na tabela de atributos com os
	 * parametros requisitados. Para PostgreSQL se o parametro oldColumnName �
	 * passado apenas o nome da coluna � atualizada, caso esse parametro seja
	 * passado vazio somente as modifica��es do tipo e do tamanho ser�o
	 * aplicados na coluna.
	 * 
	 * @param layerId
	 *            Idenficador do layer que ser� usado para a opera��o
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
	 * @return Resultado da opera��o
	 */
	public native boolean updateAttributeColumn(int layerId,
			String oldColumnName, String newColumnName, String newColumnType,
			int newColumSize, String sessionId) throws IllegalAccessException,
			InstantiationException;
	/**
	 * Variavel que deve ser definida logo ap�s a inicializa��o da Classe TerraJSP para definir se ao iniciar
	 * as inst�ncias e conex�es das sess�es ser� usado o Pool de Conex�es. Para utilizar � necess�rio definir
	 * no mesmo momento o n�mero m�ximo de conex�es para o Pool usando a fun��o setMaxPoolConnections().
	 * @param useConnectionPool Booleana se usa ou n�o o Pool
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public native void useConnectionPool(boolean useConnectionPool) throws IllegalAccessException,
			InstantiationException;
	/**
	 * N�mero m�ximo de conex�es que devem existir no Pool. � necess�rio executar essa fun��o 
	 * logo ap�s a inst�ncia da classe TerraJSP.
	 * @param maxPoolConnections
	 */
	public native void setMaxPoolConnections(int maxPoolConnections);
	/**
	 * Seta o tempo limite de espera da sess�o que necessitar de uma conex�o � n�o existir dispon�vel
	 * no Pool.Esse parametro n�o � obrigat�rio. Caso necess�rio executar essa fun��o 
	 * logo ap�s a inst�ncia da classe TerraJSP.
	 * @param maxPoolWait Tempo de timeout em Milisegundos. 
	 */
	public native void setMaxPoolWait(int maxPoolWait);
	/**
	 * Seta o n�mero m�ximo de conex�es ociosas que ficaram no servidor. Ser�o criadas inicialmente
	 * esse n�mero de conex�es. 
	 * @param maxPoolIdle N�mero m�ximo de conex�es ociosas
	 */
	public native void setMaxPoolIdle(int maxPoolIdle);
	
	public native int getRasterLayerLevels(int layerId, String sessionId) throws IllegalAccessException,
	InstantiationException;
	
	public native boolean loadNetwork(int layerId, String sessionId) throws IllegalAccessException,
	InstantiationException;
	/** 
	 * M�todo permitir o desenho de uma lista de temas (id ou nome) sem a necessidade de passar o identificador 
	 * do recurso que ser� utilizado no TerraJava/TerraManager. Isso torna a fun��o stand alone, sem a necessidade
	 * da configura��o previa do ambiente de desenho (setCurrentView(), setWorld(), setTheme(), drawCurrentTheme()).
	 * Para a execu��o desse m�todo primeiramente � necess�rio executar o m�todo connect(). Esse m�todo foi criado
	 * no intuito de executar requisi��es simult�neas de desenho (Multithread).
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
	 *  - <b>geomRep</b> Representa��o de Polygons valor = 1
	 *  - <b>colorRed</b> Componente vermelha da cor de preenchimento da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>colorGreen</b> Componente verde da cor de preenchimento da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>colorBlue</b> Componente azul da cor de preenchimento da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>styleId</b> Estilo do preenchimento da c�lula ou poligono:
	 *  	; 0 = transparente
	 *  	; 1 = preenchimento opaco
	 *  	; 2 = hachura horizontal
	 *  	; 3 = hachura vertical
	 *  	; 4 = hachura diagonal inclina��o em 135�
	 *  	; 5 = hachura diagonal inclina��o em 45�
	 *  	; 6 = hachura horizontal e vertical
	 *  	; 7 = hachura horizontal e vertical inclinada em 45�
	 * - <b>transparency</b> Cor de preenchimento aceita valores no intervalo (0 - 100), medida de porcentagem, para aplicar n�vel de transpar�ncia.
	 * - <b>contourColorRed</b> Componente vermelha da cor de contorno da geometria, valores v�lidos no intervalo (0-255).
	 * - <b>contourColorGreen</b> Componente verde da cor de contorno da geometria, valores v�lidos no intervalo (0-255).
	 * - <b>contourColorBlue</b> Componente azul da cor de contorno da geometria, valores v�lidos no intervalo (0-255).
	 * - <b>contourStyleId</b> Estilo do contorno de poligonos: 
	 *  	; 0 = linha continua
	 *  	; 1 = tracejada
	 *  	; 2 = pontilhada
	 *  	; 3 = tra�o ponto
	 *  	; 4 = tra�o ponto ponto
	 * - <b>contourTransparency</b> Cor da linha de contorno aceita valores no intervalo (0-100), medida de porcentagem, para aplicar n�vel de transpar�ncia.
	 * - <b>width</b> Largura da linha de contorno do poligono.
	 * 
	 *  <b>	PARA LINES </b>
	 *  - <b>geomRep</b> Representa��o de Lines valor = 2
	 *  - <b>colorRed</b> Componente vermelha da cor da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>colorGreen</b> Componente verde da cor da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>colorBlue</b> Componente azul da cor da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>styleId</b> Estilo das linhas:
	 *  	; 0 = linha continua
	 *  	; 1 = tracejada
	 *  	; 2 = pontilhada
	 *  	; 3 = tra�o ponto
	 *  	; 4 = tra�o ponto ponto
	 * - <b>transparency</b> Aceita valores no intervalo (0 - 100), medida de porcentagem, para aplicar n�vel de transpar�ncia.
	 * - <b>width</b> Largura da linha.
	 * 
	 * <b>	PARA POINTS </b>
	 *  - <b>geomRep</b> Representa��o de Points valor = 4
	 *  - <b>colorRed</b> Componente vermelha da cor da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>colorGreen</b> Componente verde da cor da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>colorBlue</b> Componente azul da cor da geometria, valores v�lidos no intervalo (0-255).
	 *  - <b>styleId</b> Estilo dos pontos:
	 *  	; 1 = estrela
	 *  	; 2 = circulo
	 *  	; 3 = X
	 *  	; 4 = quadrado
	 *  	; 5 = diamante
	 *  	; 6 = circulo vazado
	 *  	; 7 = quadrado vazado
	 *  	; 8 = diamente vazado
	 * - <b>transparency</b> Aceita valores no intervalo (0 - 100), medida de porcentagem, para aplicar n�vel de transpar�ncia.
	 * - <b>size</b> Tamanho do ponto
	 * 
	 *  Na qual em themeGroupingMap (Quando thematicMap = true)
	 * - <b>groupingType</b> O tipo de algoritmo de classifica��o usado para agrupar os objetos geogr�ficos.
	 * 		0 = Passos Iguais
	 * 		1 = Quantil
	 * 		2 = Desvio Padr�o
	 * 		3 = Valor �nico
	 * 		5 = Customizado

	 *	 <b>groupingAttributeType</b> Valor que define o tipo de atributo que vai ser usado para a classifica��o N�mero (0) ou Texto (1)
	 * - <b>fields</b> Nome da coluna usada para gerar o agrupamento
	 * - <b>fromClause</b> A tabela usada como tabela de atributos, a partir da qual foi especificada a coluna no par�metro jfields.
	 * - <b>linkAttr</b> Nome da coluna que permite ligar os atributos com os objetos geogr�ficos refer�nciados pelo tema corrente.
	 * - <b>restrictionExpression</b> Clausula de filtro.
	 * - <b>precision</b> N�mero de casas decimais consideradas usada na apresenta��o dos intervalos de cada faixa gerada.
	 * - <b>stdDev</b> O coeficiente de varia��o usado para permitir a compara��o entre as faixas geradas quando o algoritmo de agrupamento escolhido � o desvio padr�o.
	 * - <b>numSlices</b> O n�mero de faixas para gerar os grupos de objetos geogr�ficos. Esse par�metro somente � utilizado nos groupingType padr�o (0,1,2).
	 * - <b>rampColorsMap<b> Ramp color � uma mapa que possui booleanas que definem quais tons de cores (RGB) ser�o usados nas classes geradas pelo agrupamento. Esses tons ser�o calculados automaticamente conforme o n�mero de faixas (numSlices) selecionados e os tons (RGB). Esses par�metros somente s�o utilizados nos groupingType padr�o (0,1,2,3)   
	 * - <b>slicesList<b> Lista de mapas de faixas que ser�o classificadas as geometrias desenhadas. Esse par�metro somente � utilizado nos groupingType personalizado (5).
	 * - <b>from<b> Valor minimo da faixa para agrupar as geometrias
	 * - <b>to<b> Valor m�ximo da faixa para agrupar as geometrias
	 * - <b>count<b> Quantidade de objetos da faixa.
	 * - <b>description<b> Descri��o da faixa (Para ser gravada na legenda)
	 * - <b>sliceColorMap<b> Mapa de cores (RGB) que ser� a cor que as geometrias que forem agrupadas nessa faixa ser�o pintadas .
	 * 	
	 * Na qual em labelConfig (Quando thematicMap = true)
	 * - <b>field</b> Nome do campo da tabela de atributos que ser� usado como texto. 
	 * - <b>detectConflict</b> Habilitar ou desabilitar o controle de conflitos de texto
	 * - <b>priorityField</b> Nome do campo da tabela de atributos que ser� usado para definir qual label ter� prioridade para exibi��o caso haja conflito.
	 * - <b>urbanMode</b> Habilitar ou desabilitar o controle de visualiza��o dos textos por escala.
	 * - <b>descTextPriorityOrder</b> Variavel que define se a prioridade de sobreposi��o vai ser descrecente (DESC) ou crescente (ASC)
	 * - <b>minCollisionTol</b> Define a toler�ncia para calculo de colis�o entre labels de texto
	 * - <b>colorRed</b> Componente vermelha da cor da geometria, valores v�lidos no intervalo (0-255).
 	 * - <b>colorGreen</b> Componente verde da cor da geometria, valores v�lidos no intervalo (0-255).
	 * - <b>colorBlue</b> Componente azul da cor da geometria, valores v�lidos no intervalo (0-255).
	 * - <b>fontFamily</b> Nome da fonte ou path para fonte para ser usado para desenho do texto
	 * - <b>width</b> Tamanho da fonte que ser� usado para o desenho do texto.
	 * - <b>textBox</b> Mapa com o Box que deve ser usado para o calculo de conflito do texto (Teste parcial executado sem sucesso, variavel adicionada para tentativa futura)
	 * 
	 * @param x1 Valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y1 Valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param x2 Valor da longitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y2 Valor da latitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param width	Largura da �rea de desenho, em pixels, compat�vel com a
	 *            largura da imagem gerada para o dispositivo de visualiza��o
	 *            (tela).
	 * @param height Altura da �rea de desenho, em pixels, compat�vel com a altura
	 *            da imagem gerada para o dispositivo de visualiza��o (tela).
	 * @param keepAspectRatio Caso True ajusta as coordenadas da �rea de interesse
	 * para manter a rela��o de aspecto da imagem gerada para o dispositivo
	 * conforme os valores definidos para a largura e altura da �rea de desenho.
	 * @param from 
	 * @param linkAttr
	 * @param restrictionExpression
	 * @param imageType
	 *            Tipo de compress�o usada na imagem de sa�da.
	 * 
	 *            <pre>
	 * 0: para compress�o PNG.
	 * 1: para compress�o JPEG.
	 * 2: para compress�o GIF.
	 * </pre>
	 * @param opaque
	 *            Verdadeiro ou falso definido abaixo, conforme convencionado:
	 * 
	 *            <pre>
	 *  true: gerar imagem de legenda com fundo opaco.
	 *  false: para gerar a imagem de legenda com fundo transparente.
	 * </pre>
	 * @param quality
	 *            Valor num�rico, definido abaixo, que representa a porcentagem
	 *            de qualidade da imagem gerada, caso a sa�da seja em formato
	 *            JPEG, conforme convencionado:
	 * 
	 *            <pre>
	 * intervalo v�lido: 0 ~ 100
	 * </pre>
	 * @param projectionMap Proje��o desejada para que seja desenhado no canvas 
	 * 	          no formato de um HashMap Par�metros da proje��o em um HashMap.
	 * 
	 *            <pre>
	 * <b>Lista padronizada de par�metros (exemplo de uso):</b>
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
	 * @param canvasBackground  HashMap com a cor (r,g,b) de fundo do canvas que ser� desenhado.
	 * 
	 * <pre>
	 * <b>Lista padronizada de par�metros (exemplo de uso):</b>
	 * 
	 * HashMap<String, Object> canvasBackgroundMap = new HashMap<String, Object>();
	 * canvasBackgroundMap.put("r", 255);
	 * canvasBackgroundMap.put("g", 255);
	 * canvasBackgroundMap.put("b", 255);
	 * </pre>
	 * @param useScaleControl Permite definir a atua��o do controle de escala sobre os temas. O
	 * controle de escala permite que as geometrias representadas por um tema
	 * sejam desenhadas em intervalos de escalas pr� definidos, um intervalo por
	 * tema, melhorando o desempenho e a apresenta��o de camadas de dados que
	 * possuem n�veis de detalhamento melhor definidos em escalas diferentes.
	 * Aten��o: Atua apenas nas camadas para as quais existe defini��o de
	 * intervalo de escala.
	 *  
	 * @return Um array de bytes que representa a imagem do mapa desenhado sobre
	 *         a �rea de desenho para os temas desenhados at� o momento.
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
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
	 * Retorna a matriz do TeRaster referente ao layer com representa��o
	 * matricial (Rep = 512) do tema corrente. Se o tema corrente n�o � de um
	 * layer com representa��o matricial, a matriz retornada possui todas as
	 * celulas com valor 0.0 (matriz vazia). Este m�todo � utilizado
	 * principalmente para recuperar a matriz de valores num�ricos para a
	 * contru��o de uma representa��o tridimensional. Neste caso, o Raster
	 * armazenado seria de um Modelo Num�rico de Terreno (MNT) de altimetria de
	 * uma �rea geogr�fica. Os parametros definem qual �rea espec�fica do
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
	 *            N�mero de controle de sess�o, geralmente gerado pelo servidor
	 *            de aplica��o no momento da cria��o da sess�o do usu�rio,
	 *            quando a primeira requisi��o � feita. Deve ser um
	 *            identificador �nico.
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
	 * Metodo para desenho de textos usando a rotina de desenho paralelo, permitindo a execu��o
	 * simult�nea de N threads de desenho, sem manter estado da aplica��o na biblioteca.
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
	 * - <b>field</b> Nome do campo da tabela de atributos que ser� usado como texto. 
	 * - <b>detectConflict</b> Habilitar ou desabilitar o controle de conflitos de texto
	 * - <b>priorityField</b> Nome do campo da tabela de atributos que ser� usado para definir qual label ter� prioridade para exibi��o caso haja conflito.
	 * - <b>urbanMode</b> Habilitar ou desabilitar o controle de visualiza��o dos textos por escala.
	 * - <b>descTextPriorityOrder</b> Variavel que define se a prioridade de sobreposi��o vai ser descrecente (DESC) ou crescente (ASC)
	 * - <b>minCollisionTol</b> Define a toler�ncia para calculo de colis�o entre labels de texto
	 * - <b>colorRed</b> Componente vermelha da cor da geometria, valores v�lidos no intervalo (0-255).
 	 * - <b>colorGreen</b> Componente verde da cor da geometria, valores v�lidos no intervalo (0-255).
	 * - <b>colorBlue</b> Componente azul da cor da geometria, valores v�lidos no intervalo (0-255).
	 * - <b>fontFamily</b> Nome da fonte ou path para fonte para ser usado para desenho do texto
	 * - <b>width</b> Tamanho da fonte que ser� usado para o desenho do texto.
	 * - <b>textBox</b> Mapa com o Box que deve ser usado para o calculo de conflito do texto (Teste parcial executado sem sucesso, variavel adicionada para tentativa futura)
	 * 
	 * @param x1 Valor da longitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y1 Valor da latitude do ponto que representa o canto inferior
	 *            esquerdo da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param x2 Valor da longitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param y2 Valor da latitude do ponto que representa o canto superior
	 *            direito da �rea de interesse em coordenadas da proje��o da
	 *            vista corrente.
	 * @param width	Largura da �rea de desenho, em pixels, compat�vel com a
	 *            largura da imagem gerada para o dispositivo de visualiza��o
	 *            (tela).
	 * @param height Altura da �rea de desenho, em pixels, compat�vel com a altura
	 *            da imagem gerada para o dispositivo de visualiza��o (tela).
	 * @param keepAspectRatio Caso True ajusta as coordenadas da �rea de interesse
	 * para manter a rela��o de aspecto da imagem gerada para o dispositivo
	 * conforme os valores definidos para a largura e altura da �rea de desenho.
	 * @param from 
	 * @param linkAttr
	 * @param restrictionExpression
	 * @param imageType
	 *            Tipo de compress�o usada na imagem de sa�da.
	 * 
	 *            <pre>
	 * 0: para compress�o PNG.
	 * 1: para compress�o JPEG.
	 * 2: para compress�o GIF.
	 * </pre>
	 * @param opaque
	 *            Verdadeiro ou falso definido abaixo, conforme convencionado:
	 * 
	 *            <pre>
	 *  true: gerar imagem de legenda com fundo opaco.
	 *  false: para gerar a imagem de legenda com fundo transparente.
	 * </pre>
	 * @param quality
	 *            Valor num�rico, definido abaixo, que representa a porcentagem
	 *            de qualidade da imagem gerada, caso a sa�da seja em formato
	 *            JPEG, conforme convencionado:
	 * 
	 *            <pre>
	 * intervalo v�lido: 0 ~ 100
	 * </pre>
	 * @param projectionMap Proje��o desejada para que seja desenhado no canvas 
	 * 	          no formato de um HashMap Par�metros da proje��o em um HashMap.
	 * 
	 *            <pre>
	 * <b>Lista padronizada de par�metros (exemplo de uso):</b>
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
	 * @param canvasBackground  HashMap com a cor (r,g,b) de fundo do canvas que ser� desenhado.
	 * 
	 * <pre>
	 * <b>Lista padronizada de par�metros (exemplo de uso):</b>
	 * 
	 * HashMap<String, Object> canvasBackgroundMap = new HashMap<String, Object>();
	 * canvasBackgroundMap.put("r", 255);
	 * canvasBackgroundMap.put("g", 255);
	 * canvasBackgroundMap.put("b", 255);
	 * </pre>
	 * @param useScaleControl Permite definir a atua��o do controle de escala sobre os temas. O
	 * controle de escala permite que as geometrias representadas por um tema
	 * sejam desenhadas em intervalos de escalas pr� definidos, um intervalo por
	 * tema, melhorando o desempenho e a apresenta��o de camadas de dados que
	 * possuem n�veis de detalhamento melhor definidos em escalas diferentes.
	 * Aten��o: Atua apenas nas camadas para as quais existe defini��o de
	 * intervalo de escala.
	 *  
	 * @return Um array de bytes que representa a imagem do mapa desenhado sobre
	 *         a �rea de desenho para os temas desenhados at� o momento.
	 * 
	 * <b>Pr� requisitos:</b>
	 * 
	 * Conectar: m�todo connect() {@link #connect(String, String, String, String, int, int, String)}
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
