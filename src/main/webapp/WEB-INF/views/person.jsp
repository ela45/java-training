<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>Todos</title>
<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet">

<style>
.footer {
	position: absolute;
	bottom: 0;
	width: 100%;
	height: 60px;
	background-color: #f5f5f5;
}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
function deletePerson(id) {
	
	 $.ajax({
		    url: '/people.do' + '?' + $.param({"id": id}),
		    type: 'DELETE',
		    success: function(result) {
		    	location.reload();
		    }
		});
	}


</script>
</head>

<body>

	<nav class="navbar navbar-default">

		<a href="/" class="navbar-brand">Persons app</a>

		<ul class="nav navbar-nav">
			<li class="active"><a href="/list-person.do">Home</a></li>
			<li><a href="/insert">Add Person</a></li>
			
		</ul>

		

	</nav>

	<div class="container">
	
		<table border="1">
			<caption>
				<h2>List of Person</h2>
				${errorEmptyList}
			</caption>
			<tr>
				<th>ID</th>
				<th>Name</th>
				<th>Age</th>
				<th>Birth Date</th>
				<th>Actions</th>
			</tr>
			
			<c:forEach items="${personList}" var="person">

				<tr>
					<td><c:out value="${person.id}" /></td>
					<td><c:out value="${person.name}" /></td>
					<td><c:out value="${person.age}" /></td>

					<td><fmt:formatDate value="${person.birthDate}" type="date" />
					</td>
					<td>
						<input type="button" id="deleteBtn" value="DELETE" onClick=deletePerson(${person.id}); />

						<a href="/insert?id=${person.id}">UPDATE</a>
					</td>
				</tr>


			</c:forEach>
		</table>


	</div>

	<footer class="footer">
		<p>footer content</p>
	</footer>

	<script src="webjars/jquery/1.9.1/jquery.min.js"></script>
	<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>

</body>

</html>

