<#import "/spring.ftl" as spring/>
<#import "../macroses/commonMacro.ftlh" as commonMacro>
<@commonMacro.headerMacro/>

<form action="/admin/tour/save" method="post" name="tourDTO" id="tourDTO">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <table>
        <@spring.formHiddenInput "tourDTO.id"/>

        <tr>
            <td><label for="burning"><@spring.message "tour.form.burning"/></label><br>
                <@spring.formSingleSelect "tourDTO.burning"
                {"true":"Yes","false":"No"}
                />
            </td>
        </tr>
        <tr>
            <td>
                <label for="name"><@spring.message "tour.form.name.ukrainian"/></label><br>
                <@spring.formInput "tourDTO.name[uk_UA]"/>
            </td>
            <td>
                <label for="name"><@spring.message "tour.form.name.english"/></label><br>
                <@spring.formInput "tourDTO.name[en_GB]"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="description"><@spring.message "tour.form.description.ukrainian"/></label><br>
                <@spring.formTextarea "tourDTO.description[uk_UA]" 'rows="10" cols="30"'/>
            </td>

            <td>
                <label for="description"><@spring.message "tour.form.description.english"/></label><br>
                <@spring.formTextarea "tourDTO.description[en_GB]" 'rows="10" cols="30"'/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="amountOfPersons"><@spring.message "tour.form.amount_of_person"/></label><br>
                <@spring.formInput "tourDTO.amountOfPersons"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="price"><@spring.message "tour.form.price"/><@spring.message "money.sign"/></label><br>
                <@spring.bind path="tourDTO.price"/>
                <input type="text"
                        <#if "${requestContext.locale}"=="uk_UA">
                            value="${tourDTO.price?number/100}"
                        <#else>
                            value="${tourDTO.price?number/dollarCourse/100}"
                        </#if>
                       name="${spring.status.expression}" />

            </td>
        </tr>
        <tr>
            <#if "${requestContext.locale}"=="uk_UA">
                <td>
                    <label for="tourTypes"><@spring.message "tour.form.tour_type.header"/></label><br>
                    <@spring.formMultiSelect "tourDTO.tourTypes" {
                    "SHOPPING":"Шопінг",
                    "RECREATION":"Віпочинок",
                    "EXCURSION":"Екскурсія"}/>
                </td>
            <#else>
                <td>
                    <label for="tourTypes"><@spring.message "tour.form.tour_type.header"/></label><br>
                    <@spring.formMultiSelect "tourDTO.tourTypes" {
                    "SHOPPING":"Shopping",
                    "RECREATION":"Recreation",
                    "EXCURSION":"Exursion"}/>
                </td>
            </#if>

        </tr>
        <tr>
            <td>
                <label for="hotelType"><@spring.message "tour.form.hotel_type.header"/></label><br>
                <@spring.formSingleSelect "tourDTO.hotelType"
                {"ONE_STAR":"*",
                "TWO_STAR":"**",
                "THREE_STAR":"***",
                "FOUR_STAR":"****",
                "FIVE_STAR":"*****"}
                />
            </td>
        </tr>
        <tr>
            <td><input type="submit">
            </td>
        </tr>
    </table>
</form>
