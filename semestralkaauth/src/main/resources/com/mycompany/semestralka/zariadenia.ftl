<#-- @ftlvariable name="" type="com.mycompany.semestralka.ZariadeniaListView" -->
<html>
<head>
<meta charset="UTF-8">
<style>
table, th, td {
    border: 1px solid black;
}
</style>
</head>
    <body>
        <h1>zoznam zariadení užívateľa</h1>
        <table style="width:100%">
                    <tr><th></th><th>ID užívateľa</th><th>ID zariadenia</th><th>názov</th><th></th></tr>
                    <#list zariadenia as zariadenie>
                            <tr><th><a href="/HTML/data?idZariadenia=${zariadenie.id}">&gt&gt</a></th><th>${zariadenie.idUzivatela}</th> <th>${zariadenie.id}</th> <th>${zariadenie.nazov}</th><th><a href="/HTML/zariadenie/delete?id=${zariadenie.id}">vymazať</a></th></tr>
                    <#else>
                            <tr><th>ziadne zariadenia</th></tr>
                    </#list>
                </table>
                <h2>Vytvorenie nového zariadenia</h2>

                <form action="/HTML/zariadenie/new" method="get">
                ID užívateľa:  <br><input type="text" name="idUzivatela" id="idUzivatela" value="${(zariadenia[0].idUzivatela)!''}"/><br>
                Názov:       <br><input type="text" name="nazov" id="nazov" /><br>
                
                <input type="submit" value="Vytvor">
                </form> 
                
		<p><a href="/HTML/uzivatel">späť</a>
    </body>
</html>
