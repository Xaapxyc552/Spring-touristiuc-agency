<#import "spring.ftl" as spring/>
<#import "macroses/commonHeaderMacro.ftlh" as commonMacros>
<@commonMacros.headerMacro/>

${firstname}<br>
${email}<br>
${username}<br>
${userRole}<br>
${money}<br>

    Ваши чеки:
    <#list userChecks as check>
        ${check.tour.name}
        ${check.totalPrice}
        ${check.status.status}
    </#list>

<#--//счет,имя, роль, ссылки на другие места-->