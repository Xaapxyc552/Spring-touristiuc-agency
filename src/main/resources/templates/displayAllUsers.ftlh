<#import "spring.ftl" as spring/>
<h1>Already registered users:</h1>
<table>
    <#list usersToDisplay as user>
        <tr>
            <td>${user.firstname}</td>
            <td>${user.email}</td>
        </tr>
    </#list>
</table>
