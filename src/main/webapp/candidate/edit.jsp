<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="store.PsqlStore" %>
<%@ page import="model.Candidate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <title>Работа мечты</title>

    <script>
        function validate() {
            let result = true;
            let name = $('#name').val();
            if ((typeof name === 'undefined') || name === "") {
                alert(`Заполните поле Имя`);
                result = false;
            }
            let city = $('#city').val();
            if ((typeof city === 'undefined') || city === "" || city === null) {
                alert(`Заполните поле Город`);
                result = false;
            }
            return result;
        }

        function changeEventHandler() {
            var selectedVal= $("#city option:selected").val();
            $("#cityId").val(selectedVal);
            console.log("#cityId.val() = " + $("#cityId").val());
        }

        $(document).ready(function () {
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/dreamjob/cities.do",
                dataType: "json",
                success: function (data) {
                    let cities = "";
                    let city = $('#city').val();
                    let savedIndex = -1;
                    for (let i = 0; i < data.length; i++) {
                        let dataId = String(data[i]['id']);
                        if (dataId === city) {
                            cities += "<option selected value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                            savedIndex = i;
                        } else {
                            cities += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                        }
                    }
                    $("#city").html(cities).prop("selectedIndex", savedIndex);
                }
            });
        });
    </script>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "", 0, "");
    if (id != null) {
        candidate = PsqlStore.instOf().findByCandidateId(Integer.parseInt(id));
    }
%>
<div class="container pt-3">

    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp"> <c:out value="${requestScope.user.name}"/> | Выйти</a>
            </li>
        </ul>
    </div>

    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат.
                <% } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" id="name" name="name" value="<%=candidate.getName()%>">
                    </div>
                    <div class="form-group">
                        <label for="city">Город</label>
                        <select id="city" name="city" class="form-control" size="1" required onchange="changeEventHandler();">
                            <option value="<%=candidate.getCityId()%>" selected><%=candidate.getCity()%></option>
                        </select>
                        <input type="hidden" class="form-control" id="cityId" name="cityId" value="<%=candidate.getCityId()%>">
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
