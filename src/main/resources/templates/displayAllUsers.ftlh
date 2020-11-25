<#import "spring.ftl" as spring/>
<h1>Already registered users:</h1>
<table>
    <#list usersToDisplay as user>
        <tr>
            <td>${user.username}</td>
            <td>${user.password}</td>
        </tr>
    </#list>
</table>
