<#-- @ftlvariable name="" type="com.mycompany.semestralka.DataListView" -->
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
        <h1>zoznam dát zariadenia</h1>
        <table style="width:100%">
                <tr><th>názov</th><th>hodnota</th><th>jednotka</th><th>dátum</th><th></th></tr>
		<#list data as dat>
			<tr><th>${dat.nazov}</th> <th>${dat.hodnota}</th> <th>${dat.jednotka}</th> <th>${dat.datum}</th> <th><a href="/HTML/data/delete?id=${dat.id}">vymazať</a></th></tr>
		<#else>
			<tr><th>žiadne data</th></tr>
		</#list>
		
        </table>
        <h2>Vytvorenie nového záznamu dát</h2>

        <form action="/HTML/data/new" method="get">
        ID zariadenia:   <br>    <input type="text" name="idZariadenia" id="idZariadenia"  value="${(data[0].idZariadenia)!''}"/><br>
        Názov:   <br>    <input type="text" name="nazov" id="nazov" /><br>
        Hodnota:  <br> <input type="text" name="hodnota" id="hodnota" /><br>
        Jednotka: <br> <input type="text" name="jednotka" id="jednotka" /><br>
        <input type="submit" value="Vytvor">
        </form> 

        <p><a href="/HTML/zariadenie?idUzivatela=${idUzivatela}">späť</a>
    </body>
</html>
