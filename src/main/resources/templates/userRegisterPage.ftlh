<#import "spring.ftl" as spring/>
<#import "macroses/commonHeaderMacro.ftlh" as localeChangeMacro>


<@spring.message "message.user.registration.page.header"/>
<@spring.message "form.header"/>
<@localeChangeMacro.headerMacro/>

<form action="/user/new" method="post">
    <h1>Enter your data:</h1>
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