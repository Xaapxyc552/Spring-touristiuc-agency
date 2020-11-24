<#import "spring.ftl" as spring/>
<h1>Error during processing the request:</h1>
<table>
    <#list errors as error>
        <tr>
            <td>${error}</td>
        </tr>
    </#list>
</table>
