package top.dearbo.plugin.mybatis;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;

import java.util.List;

public class ExampleQueryPlugin extends PluginAdapter {

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.setSuperClass("BaseQuery");
        topLevelClass.addImportedType("top.dearbo.base.bean.BaseQuery");
//        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("com.hoge.UserInterface"));
//        topLevelClass.addInnerClass(new InnerClass(new FullyQualifiedJavaType("abcd")));
//        addLimit(topLevelClass, introspectedTable, "limitStart");
//        addLimit(topLevelClass, introspectedTable, "limitSize");
        return super.modelExampleClassGenerated(topLevelClass,
                introspectedTable);
    }

    private void addLimit(TopLevelClass topLevelClass,
                          IntrospectedTable introspectedTable, String name) {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        /**
         * 创建类成员变量 如protected Integer limitStart;
         */
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(PrimitiveTypeWrapper.getIntegerInstance());
        field.setName(name);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        //首字母大写
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);

        //添加Setter方法
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("set" + camel);
        method.addParameter(new Parameter(PrimitiveTypeWrapper
                .getIntegerInstance(), name));

        StringBuilder sb = new StringBuilder();
        sb.append("this.");
        sb.append(name);
        sb.append(" = ");
        sb.append(name);
        sb.append(";");
        //如 this.limitStart = limitStart;
        method.addBodyLine(sb.toString());

        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        //添加Getter Method 直接调用AbstractJavaGenerator的getGetter方法
        Method getterMethod = AbstractJavaGenerator.getGetter(field);
        commentGenerator.addGeneralMethodComment(getterMethod,
                introspectedTable);
        topLevelClass.addMethod(getterMethod);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

}
