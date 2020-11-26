<#macro changeLanguageMacro>
    <table>
        <form action="/locale/" method="post">
            <tr>
                <td>
                    <select name="lang" id="lang">

                        <option value="en-GB">English</option>
                        <option value="uk-UA">Українська</option>
                    </select>
                </td>
                <td>
                    <input type="submit" value="Select">
                </td>
            </tr>
        </form>


    </table>
</#macro>

