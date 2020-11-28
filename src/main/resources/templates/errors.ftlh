<#import "spring.ftl" as spring/>
<#import "macroses/commonHeaderMacro.ftlh" as commonMacros>
<@commonMacros.headerMacro/>

<h1>Error during processing the request:</h1>
<table>
    <#list errors as error>
        <tr>
            <td>${error}</td>
        </tr>
    </#list>
</table>
