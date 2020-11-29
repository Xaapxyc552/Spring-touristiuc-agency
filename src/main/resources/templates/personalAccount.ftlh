<#import "spring.ftl" as spring/>
<#import "macroses/commonMacro.ftlh" as commonMacros>
<@commonMacros.headerMacro/>

${firstname}<br>
${email}<br>
${username}<br>
${userRole}<br>
${money}<br>

<@commonMacros.pagingMacro pagesSequence=pagesSequence currentPage=currentPage  href="/user/personal-account/"/>

<table>
    <@spring.message "check.header"/> <br>
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
        <#if check.status.status.name()="WAITING_FOR_CONFIRM">
            <tr>
                <th>
                    <form action="/tours/remove/${check.id}" method="post">

                        <input type="submit" value="<@spring.message "check.remove"/>">

                    </form>
                </th>
            </tr>
        </#if>
        <tr>
            <th><br></th>
        </tr>
        <#else> <@spring.message "check.you_dont_have_bookings_yet"/>
    </#list>
</table>
<#--//счет,имя, роль, ссылки на другие места-->