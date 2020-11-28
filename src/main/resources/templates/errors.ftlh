<#import "spring.ftl" as spring/>
<h1><@spring.message "exception.during.processing"/></h1>
<table>
    <#list errors as error>
        <tr>
            <td>${error}</td>
        </tr>
    </#list>
</table>
