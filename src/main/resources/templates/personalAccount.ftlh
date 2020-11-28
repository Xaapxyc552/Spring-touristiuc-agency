<#import "spring.ftl" as spring/>
<#import "macroses/commonHeaderMacro.ftlh" as commonMacros>
<@commonMacros.headerMacro/>

${firstname}<br>
${email}<br>
${username}<br>
${userRole}<br>
${money}<br>
<table>
    <@spring.message "check.header"/>
    <#list userChecks as check>
        <tr>
            <th><@spring.message "check.tour_name"/></th>
            <th>${check.tour.name}</th>
        </tr>
        <tr>
            <th><@spring.message "check.total_price"/></th>
            <th>${check.totalPrice}</th>
        </tr>
        <tr>
            <th><@spring.message "check.status"/></th>
            <th>${check.status.status}</th>
        </tr>
        <tr>
            <th><br></th>
        </tr>
    </#list>
</table>
<#--//счет,имя, роль, ссылки на другие места-->