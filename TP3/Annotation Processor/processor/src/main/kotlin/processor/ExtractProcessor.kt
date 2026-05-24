package processor

import annotations.Extract
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_23)
@SupportedAnnotationTypes("annotations.Extract")
class ExtractProcessor : AbstractProcessor() {
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val classMethodMap = mutableMapOf<TypeElement, MutableList<ExecutableElement>>()

        // Find all methods annotated with @Greeting
        for (element in roundEnv.getElementsAnnotatedWith(Extract::class.java)) {
            if (element is ExecutableElement) {
                val enclosingClass = element.enclosingElement as TypeElement
                classMethodMap.computeIfAbsent(enclosingClass) {
                    mutableListOf()
                }.add(element)
            }
        }

        // Generate wrapper classes for each class containing annotated methods
        for ((classElement, methods) in classMethodMap) {
            generateExtractorClass(classElement, methods)
        }
        return true
    }

    private fun generateExtractorClass(
        classElement: TypeElement,
        methods: List<ExecutableElement>
    ) {
        val packageName = processingEnv.elementUtils.getPackageOf(classElement).toString()
        val originalClassName = classElement.simpleName.toString()
        val DataProcessorExtractor = "${originalClassName}Extractor"

        // Create the extract class using composition
        val classBuilder = TypeSpec.classBuilder(DataProcessorExtractor)
            .superclass(ClassName(packageName, originalClassName))
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("input", String::class)
                    .build()
            )
            .addSuperclassConstructorParameter("input")
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)

        // Generate extract methods
        for (method in methods) {
            val methodName = method.simpleName.toString()

            val regexValue = method.getAnnotation(Extract::class.java)?.regex ?: ""

            val methodBuilder = FunSpec.builder(methodName)
                .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
                .returns(String::class.asTypeName().copy(nullable = true)) //this line was added using gemini ai during troubleshooting (kotlin assumed func returned void and now it makes it return string)
                .addStatement("val match = Regex(%S).find(input)", regexValue)
                .addStatement("return match?.groupValues?.get(1)")

            classBuilder.addFunction(methodBuilder.build())
        }

        // Build the Kotlin file
        val file = FileSpec.builder(packageName, DataProcessorExtractor)
            .addType(classBuilder.build())
            .build()

        // Write the generated file
        try {
            val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
            if (kaptKotlinGeneratedDir != null) {
                file.writeTo(File(kaptKotlinGeneratedDir))
            } else {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "kapt.kotlin.generated not found"
                )
            }
        } catch (e: Exception) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Error generating Kotlin file: ${e.message}"
            )
        }
    }
}