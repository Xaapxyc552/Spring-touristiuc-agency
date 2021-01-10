<#import "spring.ftl" as spring/>
<#import "macroses/commonMacro.ftlh" as commonMacro>

<#function contains item>
    <#list selectedTourTypes as nextInList>
        <#if nextInList == item><#return true></#if>
    </#list>
    <#return false>
</#function>

<#function areEquals item1 item2>
    <#if item1?? && item2?? && item1?lower_case==item2?lower_case><#return true></#if>
    <#return false>
</#function>


<body>
<@commonMacro.headerMacro/>
<form action="/tours/list/1" method="get">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <@spring.message "tour.form.ordering.header"/>:
    <select name="order" id="order">
        <option value="AMOUNT_OF_PERSONS"<#if areEquals(order,'AMOUNT_OF_PERSONS')> selected="selected"</#if> > <@spring.message "tour.amount_of_persons"/></option>
        <option value="PRICE"<#if areEquals(order,'PRICE')> selected="selected"</#if>><@spring.message "tour.price"/></option>
        <option value="HOTEL_TYPE"<#if areEquals(order,'HOTEL_TYPE')> selected="selected"</#if>><@spring.message "tour.hotel_type"/></option>
    </select>
    <@spring.message "tour.form.direction.header"/>:
    <select name="direction" id="direction">
        <option value="DESC" <#if areEquals(direction,'DESC')> selected="selected"</#if>><@spring.message "tour.form.direction.desc"/></option>
        <option value="ASC" <#if areEquals(direction,'ASC')> selected="selected"</#if>><@spring.message "tour.form.direction.asc"/></option>
    </select>
    <br>


    <input type="checkbox" name="selectedTourTypes" value="SHOPPING" <#if contains('SHOPPING')>checked="checked" </#if>>
    <@spring.message "tour.tour_type.SHOPPING"/> <br>
    <input type="checkbox" name="selectedTourTypes" value="RECREATION"
           <#if contains('RECREATION')>checked="checked" </#if>>
    <@spring.message "tour.tour_type.RECREATION"/><br>
    <input type="checkbox" name="selectedTourTypes" value="EXCURSION"
           <#if contains('EXCURSION')>checked="checked" </#if>>
    <@spring.message "tour.tour_type.EXCURSION"/> <br>
    <input type="submit" value="<@spring.message "tour.form.direction.submit"/>">

</form>

<@commonMacro.pagingMacro pagesSequence=pagesSequence currentPage=currentPage  href="/tours/list/"/>

<table border="3" style="word-wrap: break-word;" width="500px">
    <#list tours as tour>
        <tr <#if tour.burning> style="color:#ff0000" </#if>>
            <td><@spring.message "tour.burning"/></td>
            <td><@spring.message "tour.burning.${tour.burning?c}"/></td>
        </tr>
        <tr>
            <td><@spring.message "tour.name"/></td>
            <#if "${requestContext.locale}"=="uk_UA">
                <td>${tour.name["uk_UA"]}</td>
            <#else>
                <td>${tour.name["en_GB"]}</td>
            </#if>
        </tr>
        <tr>
            <td><@spring.message "tour.description"/></td>
            <#if "${requestContext.locale}"=="uk_UA">
                <td style="width:200px;">${ tour.description["uk_UA"]}</td>
            <#else>
                <td style="width:200px;">${ tour.description["en_GB"]}</td>
            </#if>
        </tr>
        <tr>
            <td><@spring.message "tour.tour_type"/></td>
            <td>
                <#list tour.tourTypes as tourType>
                    <@spring.message "tour.tour_type.${tourType.type.name()}"/><br>
                </#list>
            </td>
        </tr>

        <tr>
            <td><@spring.message "tour.price"/></td>
            <td>
                <@commonMacro.showMoneyByLocale tour.price "${requestContext.locale}" dollarCourse/>
            </td>
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

            <#if userRole == "ROLE_USER">
                <td>
                    <form action="/tours/book/${tour.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" value="<@spring.message "tour.book_tour"/>">
                    </form>
                </td>
            </#if>
            <#if userRole == "ROLE_ADMIN">
                <td>
                    <form action="/admin/tour/edit/${tour.id}" method="get">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" value="<@spring.message "tour.edit"/>">
                    </form>
                </td>
            </#if>

            <#if userRole == "ROLE_ADMIN">
                <td>
                    <form action="/admin/tour/delete/${tour.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" value="<@spring.message "tour.delete"/>">
                    </form>
                </td>
            </#if>
        </tr>
    <#else><@spring.message "check.empty_list"/>
    </#list>
</table>
</body>
</html>

