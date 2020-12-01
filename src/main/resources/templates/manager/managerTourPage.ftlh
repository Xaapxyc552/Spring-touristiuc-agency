<#import "/spring.ftl" as spring/>
<#import "../macroses/commonMacro.ftlh" as commonMacro>
<@commonMacro.headerMacro/>

<@commonMacro.pagingMacro pagesSequence=pagesSequence currentPage=currentPage  href="/manager/tours-operations/"/>
<table>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <#list waitingChecks as check>
        <tr>
            <th><@spring.message "check.owner_username"/></th>
            <th>${check.user.username}</th>
        </tr>
        <tr>
            <th><@spring.message "check.owner_email"/></th>
            <th>${check.user.email}</th>
        </tr>
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
                    <form action="/manager/check/decline/${check.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" value="<@spring.message "check.decline"/>">
                    </form>
                </th>
                <th>
                    <form action="/manager/check/confirm/${check.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" value="<@spring.message "check.confirm"/>">
                    </form>
                </th>
            </tr>
        </#if>
        <tr>
            <th><br></th>
        </tr>
    <#else>
        <@spring.message "check.empty_list"/>
    </#list>
</table>
