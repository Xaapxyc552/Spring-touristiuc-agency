<#import "spring.ftl" as spring/>
<#import "macroses/commonMacro.ftlh" as commonMacros>
<@commonMacros.headerMacro/>

<@spring.message "personal_page.firstname"/>${firstname}<br>
<@spring.message "personal_page.email"/>${email}<br>
<@spring.message "personal_page.username"/>${username}<br>
<@spring.message "personal_page.role"/>${userRole}<br>
<@spring.message "personal_page.money"/>${money}<br>
<br>
<form action="/user/recharge" method="post">
    <@spring.message "personal_page.recharge"/><br>
    <label for="amountOfCharge"><input type="number" id="amountOfCharge" name="amountOfCharge"></label>
    <input type="submit" value="<@spring.message "personal_page.recharge"/>">
</form>


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