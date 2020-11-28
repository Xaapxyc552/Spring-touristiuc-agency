<#import "/spring.ftl" as spring/>
<#macro headerMacro>
    <table border="3">
        <form action="/locale/" method="get">
            <th>
                    <select name="lang" id="lang">
                        <option value="en-GB">English</option>
                        <option value="uk-UA">Українська</option>
                    </select>
                    <input type="submit" value="Select">
                    </td>
            </th>
                <tr>
                    <td> <#if (userRole == "ROLE_ANONYMOUS")>
                            <a href="/login"><@spring.message "common.head.login"/></a>
                            <a href="/user/new"><@spring.message "common.head.sign_in"/></a>
                        <#else>
                            <@spring.message "common.you_login_as"/> ${username}
                            <a href="/user/personal-account"><@spring.message "common.personal_account"/></a>
                            <a href="/logout"><@spring.message "common.logout"/></a>
                        </#if>
                    </td>
                </tr>
        </form>
    </table>
</#macro>

