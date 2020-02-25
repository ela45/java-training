<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>People</title>
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
function savePerson() {
	
	 $.ajax({
		    url: '/people.do' + '?' + $.param({"id": $("#id").val(), "name":$("#name").val(), "age":$("#age").val(),"birthDate":$("#birthDate").val() }),
		    type: 'POST',
		    success: function(result) {
		
		    	alert("Person inserted");
		    }
		});
	}
function updatePerson() {
	
	 $.ajax({
		    url: '/people.do' + '?' + $.param({"id": $("#id").val(), "name":$("#name").val(), "age":$("#age").val(),"birthDate":$("#birthDate").val() }),

		    type: 'PUT',
		    success: function(result) {
		        alert("Person updated");
		    	
		    }
		});
	}

</script>
</head>

<body>

	<nav class="navbar navbar-default">

		<a href="/" class="navbar-brand">Persons app</a>

		<ul class="nav navbar-nav">
			<li class="active"><a href="/people.do">Home</a></li>
			<li><a href="/insert">Add Person</a></li>
		
		</ul>

		

	</nav>

	<div class="container">
	<form name="personForm"> 

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
                    <input id="id" type="hidden" name="id" value="<c:out value='${person.id}' />" />
                </c:if>           

            <tr>
                <th>Name: </th>
                <td>
                    <input id="name" type="text" name="name" size="20"
                            value="<c:out value='${person.name}' />"
                        />
                </td>
            </tr>
            <tr>
                <th>Age: </th>
                <td>
                    <input id="age" type="text" name="age" size="3"
                            value="<c:out value='${person.age}' />"
                    />
                </td>
            </tr>
            <tr>
                <th>Date: </th>
                <td>
                    <input id="birthDate" type="text" name="birthDate" size="15"
                            value="<c:out value='${person.birthDate}' />"
                    />
                    format: yyyy-MM-dd
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                <c:if test="${person != null}">
                 <input type="submit" value="Update" onClick=updatePerson() />
                </c:if>
                <c:if test="${person == null}">
                 <input type="submit" value="Save" onClick=savePerson() />
                </c:if>
                
                   
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