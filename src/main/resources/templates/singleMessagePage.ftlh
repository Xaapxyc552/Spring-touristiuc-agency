<#import "spring.ftl" as spring/>
<#if message?? && href??>
    <h2><@spring.message message/></h2>
    <a href="${href}"><@spring.message hrefDescription/></a><br>
</#if>

<a href="/tours/list/1"><@spring.message "common.navigation.tours"/></a> <br>
