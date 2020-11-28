<#import "spring.ftl" as spring/>
<#import "macroses/commonHeaderMacro.ftlh" as localeChangeMacro>
<@localeChangeMacro.headerMacro/>

<html>
<head>
    <style>
        div {
            max-width: 350px;
        }
    </style>
</head>
<body>
<form action="/tours/list/1" method="get">
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
<#--<#if currentOrder??>-->
<#--    <span>-->
<#--        <@spring.message "tour.current.ordering"/>: ${currentOrder}-->
<#--    </span>-->
<#--    <span>-->
<#--        <@spring.message "tour.current.direction"/>: ${currentDirection}-->
<#--    </span>-->
<#--</#if>-->
<div>
    <table>
        <#list tours as tour><#--TODO нужно ли выделение горящих туров?-->
            <tr <#if tour.burning> style="color:#ff0000" </#if>>
                <th><@spring.message "tour.burning"/></th>
                <th>${tour.burning?c}</th>
            </tr>
            <tr <#if tour.burning> style="color:#ff0000" </#if>>
                <th><@spring.message "tour.name"/></th>
                <th>${tour.name}</th>
            </tr>
            <tr>
                <th><@spring.message "tour.tour_type"/></th>
                <th>
                <#list tour.tourTypes as tourType>
                   ${tourType.type.name()}<br>
                </#list>
                </th>
            </tr>
            <tr>
                <th><@spring.message "tour.price"/></th>
                <th>${tour.price}</th>
            </tr>
            <tr>
                <th><@spring.message "tour.hotel_type"/></th>
                <th>${tour.hotelType.amountOfStarsPretty}</th>
            </tr>
            <tr>
                <th><@spring.message "tour.amount_of_persons"/></th>
                <th>${tour.amountOfPersons}</th>
            </tr>
            <tr>
                <th><@spring.message "tour.description"/></th>
                <th>${tour.description}</th>
            </tr>
            <th>
                <#if userRole == "ROLE_USER">
                    <a href="/tours/book/${tour.id}"><@spring.message "tour.book_tour"/></a>
                </#if>
            </th>
            <tr>
                <th><br></th>
            </tr>
        </#list>
        <@spring.message "page.description"/>
        <#list pagesSequence as pageNumber>
            <#if pageNumber!=currentPage>
                <a href="/tours/list/${pageNumber}">   ${pageNumber}   </a>
            <#else>${pageNumber}
            </#if>
        </#list>
    </table>
</div>
</body>
</html>