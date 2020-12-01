<#import "/spring.ftl" as spring/>
<#import "../macroses/commonMacro.ftlh" as commonMacro>
<@commonMacro.headerMacro/>

<form action="/admin/tour/save" method="post" name="tourDTO" id="tourDTO">
    <table>
        <@spring.formHiddenInput "tourDTO.id"/>

        <tr>
            <td><label for="name"><@spring.message "tour.form.name"/></label><br>
                <@spring.formInput "tourDTO.name"/>
            </td>
        </tr>
        <tr>
            <td><label for="burning"><@spring.message "tour.form.burning"/></label><br>
                <@spring.formSingleSelect "tourDTO.burning"
                {"true":"Yes","false":"No"}
                />
            </td>
        </tr>
        <tr>
            <td><label for="description"><@spring.message "tour.form.description"/></label><br>
                <@spring.formTextarea "tourDTO.description" 'rows="10" cols="30"'/>
            </td>
        </tr>
        <tr>
            <td><label for="amountOfPersons"><@spring.message "tour.form.amount_of_person"/></label><br>
                <@spring.formInput "tourDTO.amountOfPersons"/>
            </td>
        </tr>
        <tr>
            <td><label for="price"><@spring.message "tour.form.price"/></label><br>
                <@spring.formInput "tourDTO.price"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="tourTypes"><@spring.message "tour.form.tour_type.header"/></label><br>
                <@spring.formMultiSelect "tourDTO.tourTypes"
                {"SHOPPING":"SHOPPING","RECREATION":"RECREATION","EXCURSION":"EXCURSION"}
                />
            </td>
        </tr>
        <tr>
            <td><label for="hotelType"><@spring.message "tour.form.hotel_type.header"/></label><br>
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
