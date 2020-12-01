<#import "/spring.ftl" as spring/>
<#import "../macroses/commonMacro.ftlh" as commonMacro>
<@commonMacro.headerMacro/>

<form action="/admin/new-tour/create" method="post">
    <table>
        <tr>
            <td><label for="name"><@spring.message "tour.form.name"/></label><br>
                <input type="text" id="name" name="name">
            </td>
        </tr>
        <tr>
            <td><label for="description"><@spring.message "tour.form.description"/></label><br>
                <textarea name="description" id="description" cols="30" rows="10"></textarea>
            </td>
        </tr>
        <tr>
            <td><label for="amountOfPersons"><@spring.message "tour.form.amount_of_person"/></label><br>
                <input type="number" id="amountOfPersons" name="amountOfPersons">
            </td>
        </tr>
        <tr>
            <td><label for="price"><@spring.message "tour.form.price"/></label><br>
                <input type="number" id="price" name="price">
            </td>
        </tr>
        <tr>
            <td>
                <label for="tourTypes"><@spring.message "tour.form.tour_type.header"/></label><br>
                <select multiple="multiple" name="tourTypes" id="tourTypes">
                    <option value="SHOPPING"><@spring.message "tour.form.tour_type.shopping"/></option>
                    <option value="RECREATION"><@spring.message "tour.form.tour_type.recreation"/></option>
                    <option value="EXCURSION"><@spring.message "tour.form.tour_type.excursion"/></option>
                </select>
            </td>
        </tr>
        <tr>
            <td><label for="hotelType"><@spring.message "tour.form.hotel_type.header"/></label><br>
                <select name="hotelType" id="hotelType">
                    <option value="ONE_STAR"><@spring.message "tour.form.hotel_type.one_star"/></option>
                    <option value="TWO_STAR"><@spring.message "tour.form.hotel_type.two_star"/></option>
                    <option value="THREE_STAR"><@spring.message "tour.form.hotel_type.three_star"/></option>
                    <option value="FOUR_STAR"><@spring.message "tour.form.hotel_type.four_star"/></option>
                    <option value="FIVE_STAR"><@spring.message "tour.form.hotel_type.five_star"/></option>
                </select>
            </td>
        </tr>
        <tr>
            <td><input type="submit">
            </td>
        </tr>
    </table>
</form>
