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
            <#if "${requestContext.locale}"=="uk_UA">
                <td>${check.tour.name["uk_UA"]}</td>
            <#else>
                <td>${check.tour.name["en_GB"]}</td>
            </#if>
        </tr>
        <tr>
            <th><@spring.message "check.total_price"/></th>
            <th>
                <@commonMacro.showMoneyByLocale check.totalPrice "${requestContext.locale}" dollarCourse/>
            </th>
        </tr>
        <tr>
            <th><@spring.message "check.status"/></th>
            <th><@spring.message "check.status.${check.status.status}"/></th>
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
