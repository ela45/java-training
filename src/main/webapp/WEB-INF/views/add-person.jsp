<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	
		<c:if test="${person != null}">
              
			<form action="update-person.do" method="post">                    
		</c:if>
		                    
		<c:if test="${person == null}">
                      

		<form action="add-person.do" method="post">                    
		</c:if>


<table border="1" cellpadding="5">
            <caption>
                <h2>
                    <c:if test="${person != null}">
                        Edit Person
                    </c:if>
                    <c:if test="${person == null}">
                        Add New Person
                    </c:if>
                </h2>
            </caption>
                <c:if test="${person != null}">
                    <input type="hidden" name="id" value="<c:out value='${person.id}' />" />
                </c:if>           

            <tr>
                <th>Name: </th>
                <td>
                    <input type="text" name="name" size="20"
                            value="<c:out value='${person.name}' />"
                        />
                </td>
            </tr>
            <tr>
                <th>Age: </th>
                <td>
                    <input type="text" name="age" size="3"
                            value="<c:out value='${person.age}' />"
                    />
                </td>
            </tr>
            <tr>
                <th>Date: </th>
                <td>
                    <input type="text" name="birthDate" size="15"
                            value="<c:out value='${person.birthDate}' />"
                    />
                    format: yyyy-MM-dd
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Save" />
                </td>
            </tr>
        </table>


		</form>
	</div>

	<footer class="footer">
		<p>footer content</p>
	</footer>

	<script src="webjars/jquery/1.9.1/jquery.min.js"></script>
	<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>

</body>

</html>