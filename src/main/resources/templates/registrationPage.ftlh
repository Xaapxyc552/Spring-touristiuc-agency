<#import "spring.ftl" as spring/>
<#import "macroses/commonMacro.ftlh" as localeChangeMacro>

<@spring.message "message.user.registration.page.header"/>
<@localeChangeMacro.headerMacro/>
<h1>
    <@spring.message "form.header"/>
</h1>
<form action="/signup" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <h1></h1>
    <table>
        <tr>
            <td><label for="firstname"><@spring.message "form.firstname"/></label><br>
                <input type="text" id="firstname" name="firstname">
            </td>
        </tr>
        <tr>
            <td><label for="username"><@spring.message "form.username"/></label><br>
                <input type="text" id="username" name="username">
            </td>
        </tr>
        <tr>
            <td><label for="email"><@spring.message "form.email"/></label><br>
                <input type="email" id="email" name="email">
            </td>
        </tr>
        <tr>
            <td><label for="password"><@spring.message "form.password"/></label><br>
                <input type="password" id="password" name="password">
            </td>
        </tr>
        <tr>
            <td><input type="submit">
            </td>
        </tr>
    </table>
</form>