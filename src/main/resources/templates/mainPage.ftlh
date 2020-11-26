<#import "spring.ftl" as spring/>
<#import "macroses/localeChangeMacro.ftlh" as localechangeMacro>
<h1>HELLO THIS IS MAIN PAGE <br>
    <a href="/user/new">Register new user</a>
</h1>
<@spring.message "message.greeting"/>
<@localechangeMacro.changeLanguageMacro/>

