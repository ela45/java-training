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
</head>

<body>

	<nav class="navbar navbar-default">

		<a href="/" class="navbar-brand">Persons app</a>

		<ul class="nav navbar-nav">
			<li class="active"><a href="/list-person.do">Home</a></li>
			<li><a href="/add-person.do">Add Person</a></li>
			
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
						<a href="delete-person.do?id=${person.id}">DELETE</a><br>
						<a href="add-person.do?id=${person.id}">UPDATE</a>
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

