<#import "spring.ftl" as spring/>
<#import "macroses/commonMacro.ftlh" as commonMacro>
<body>
<@commonMacro.headerMacro/>
<form action="/tours/list/1" method="get">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <@spring.message "tour.form.ordering.header"/>:
    <select name="order" id="order">
        <option value="AMOUNT_OF_PERSONS"><@spring.message "tour.amount_of_persons"/>:</option>
        <option value="PRICE"><@spring.message "tour.price"/></option>
        <option value="HOTEL_TYPE"><@spring.message "tour.hotel_type"/></option>
        <option value="TOUR_TYPE"><@spring.message "tour.tour_type"/></option>
    </select>
    <@spring.message "tour.form.direction.header"/>:
    <select name="direction" id="direction">
        <option value="DESC"><@spring.message "tour.form.direction.desc"/></option>
        <option value="ASC"><@spring.message "tour.form.direction.asc"/></option>
    </select>
    <input type="submit" value="Submit">
</form>

<@commonMacro.pagingMacro pagesSequence=pagesSequence currentPage=currentPage  href="/tours/list/"/>

<table border="3" style="word-wrap: break-word;" width="500px">
    <#list tours as tour><#--TODO нужно ли выделение горящих туров?-->
    <tr <#if tour.burning> style="color:#ff0000" </#if>>
        <td><@spring.message "tour.burning"/></td>
        <td>${tour.burning?c}</td>
    </tr>
    <tr <#if tour.burning> style="color:#ff0000" </#if>>
        <td><@spring.message "tour.name"/></td>
        <td>${tour.name}</td>
    </tr>
    <tr>
        <td><@spring.message "tour.tour_type"/></td>
        <td>
            <#list tour.tourTypes as tourType>
                ${tourType.type.name()}<br>
            </#list>
        </td>
    </tr>
    <tr>
        <td><@spring.message "tour.price"/></td>
        <td>${tour.price}</td>
    </tr>
    <tr>
        <td><@spring.message "tour.hotel_type"/></td>
        <td>${tour.hotelType.amountOfStarsPretty}</td>
    </tr>
    <tr>
        <td><@spring.message "tour.amount_of_persons"/></td>
        <td>${tour.amountOfPersons}</td>
    </tr>
    <tr>
        <td><@spring.message "tour.description"/></td>
        <td style="width:200px;">${tour.description}</td>
    </tr>
    <tr>
        <td>
            <#if userRole == "ROLE_USER">
                <form action="/tours/book/${tour.id}" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" value="<@spring.message "tour.book_tour"/>">
                </form>
            </#if>
            <#if userRole == "ROLE_ADMIN">
                <form action="/admin/tour/edit/${tour.id}" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" value="<@spring.message "tour.edit"/>">
                </form>

            </#if>
        </td>
        <td><#if userRole == "ROLE_ADMIN">
            <form action="/admin/tour/delete/${tour.id}" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" value="<@spring.message "tour.delete"/>">
            </form>

            </#if></td>
    </tr>
    <tr>
        <th></th>
        <th></th>
    </tr>
    <#else><@spring.message "check.empty_list"/>
    </#list>
</table>
</body>
</html>

