<#-- @ftlvariable name="" type="com.mycompany.semestralka.UzivateliaListView" -->
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
		<h1>zoznam užívateľov</h1>
                <table style="width:100%">
                    <tr><th></th><th>ID užívateľa</th><th>meno</th><th>priezvisko</th><th></th></tr>
                    <#list uzivatelia as uzivatel>
                            <tr><th><a href="/HTML/zariadenie?idUzivatela=${uzivatel.id}">&gt&gt</a></th><th>${uzivatel.id}</th> <th>${uzivatel.meno}</th> <th>${uzivatel.priezvisko}</th><th><a href="/HTML/uzivatel/delete?id=${uzivatel.id}">vymazať</a></th></tr>
                    <#else>
                            <tr><th>žiadny užívatelia</th></tr>
                    </#list>
                </table>
	<h2>Vytvorenie nového užívateľa</h2>

        <form action="/HTML/uzivatel/new/"  method="get">
         Meno:       <br><input type="text" name="meno" id="meno" /><br>
         Priezvisko: <br><input type="text" name="priezvisko" id="priezvisko" /><br>
        <input type="submit" value="Vytvor">
        </form> 

    </body>
</html>
