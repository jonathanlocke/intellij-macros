package com.telenav.intellij;

import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.JavaCodeContextType;
import com.intellij.codeInsight.template.JavaCommentContextType;
import com.intellij.codeInsight.template.JavaStringContextType;
import com.intellij.codeInsight.template.Result;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.codeInsight.template.TextResult;
import com.intellij.codeInsight.template.macro.MacroBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WithMacro extends MacroBase
{
    public WithMacro()
    {
        super("JAVA-DESIGN-PATTERNS-WITH-X", "Adds a copy constructor, copy() method and withX() methods for all fields");
    }

    @Override
    public boolean isAcceptableInContext(final TemplateContextType context)
    {
        return context instanceof JavaCodeContextType
                || context instanceof JavaCommentContextType
                || context instanceof JavaStringContextType;
    }

    @Override
    protected @Nullable
    Result calculateResult(final Expression[] parameters,
                           final ExpressionContext context,
                           final boolean quick)
    {
        var file = context.getPsiElementAtStartOffset().getContainingFile();
        var methods = new StringList();
        file.accept(new PsiRecursiveElementWalkingVisitor()
        {
            @Override
            public void visitElement(@NotNull final PsiElement element)
            {
                if (element instanceof PsiField field)
                {
                    methods.add("name = $", field.getName());
                }
            }
        });

        return new TextResult(methods.asString());
    }
}
