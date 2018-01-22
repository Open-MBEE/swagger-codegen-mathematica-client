package org.openmbee.swagger.codegen;

import io.swagger.codegen.*;
import io.swagger.models.properties.*;

import java.io.File;

public class MathematicaClientGenerator extends DefaultCodegen implements CodegenConfig {
    protected String apiVersion = "0.1.0";
    private String invokerPackage;

    /**
     * Configures the type of generator.
     *
     * @return the CodegenType for this generator
     * @see io.swagger.codegen.CodegenType
     */
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -l flag.
     *
     * @return the friendly name for the generator
     */
    public String getName() {
        return "mathematica-client";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    public String getHelp() {
        return "Generates a Mathematica client library.";
    }

    public MathematicaClientGenerator() {
        super();
        outputFolder = "generated-code" + File.separator + "mathematica";

        cliOptions.add(new CliOption(CodegenConstants.INVOKER_PACKAGE, CodegenConstants.INVOKER_PACKAGE_DESC));

        /**
         * Models.  You can write model files using the modelTemplateFiles map.
         * if you want to create one template for file, you can do so here.
         * for multiple files for model, just put another entry in the `modelTemplateFiles` with
         * a different extension
         */
        modelTemplateFiles.put(
                "model.mustache", // the template to use
                ".m");       // the extension for each file to write

        /**
         * Api classes.  You can write classes for each Api file with the apiTemplateFiles map.
         * as with models, add multiple entries with different extensions for multiple files per
         * class
         */
        apiTemplateFiles.put(
                "api.mustache",   // the template to use
                ".m");       // the extension for each file to write

        /**
         * Template Location.  This is the location which templates will be read from.  The generator
         * will use the resource stream to attempt to read the templates.
         */
        templateDir = "mathematica-client";

        /**
         * Reserved words.  Override this with reserved words specific to your language
         */
        /*
        reservedWords = new HashSet<>(
                Arrays.asList(
                        "sample1",  // replace with static values
                        "sample2")
        );
        */

        /**
         * Additional Properties.  These values can be passed to the templates and
         * are available in models, apis, and supporting files
         */
        additionalProperties.put("apiVersion", apiVersion);
        additionalProperties.put(CodegenConstants.INVOKER_PACKAGE, invokerPackage);

        /**
         * Supporting Files.  You can write single files for the generator with the
         * entire object tree available.  If the input file has a suffix of `.mustache
         * it will be processed by the template engine.  Otherwise, it will be copied
         */
        /*
        supportingFiles.add(new SupportingFile("auth.mustache",   // the input template or file
                "",                                                       // the destination folder, relative `outputFolder`
                "AuthenticationMethods.m")                                          // the output file
        );
        */

        /**
         * Language Specific Primitives.  These types will not trigger imports by
         * the client generator
         */
        /*
        languageSpecificPrimitives = new HashSet<String>(
                Arrays.asList(
                        "Type1",      // replace these with your types
                        "Type2")
        );
        */
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.containsKey(CodegenConstants.INVOKER_PACKAGE)) {
            this.setInvokerPackage((String) additionalProperties.get(CodegenConstants.INVOKER_PACKAGE));
        }
        supportingFiles.add(new SupportingFile("main.mustache", "", (invokerPackage != null ? invokerPackage : "Main") + ".m"));
    }

    public void setInvokerPackage(String invokerPackage) {
        this.invokerPackage = invokerPackage;
    }

    /**
     * Escapes a reserved word as defined in the `reservedWords` array. Handle escaping
     * those terms here.  This logic is only called if a variable matches the reseved words
     *
     * @return the escaped term
     */
    @Override
    public String escapeReservedWord(String name) {
        return "_" + name;  // add an underscore to the name
    }

    @Override
    public String toOperationId(String operationId) {
        return camelize(sanitizeName(operationId), false);
    }

    /**
     * Optional - type declaration.  This is a String which is used by the templates to instantiate your
     * types.  There is typically special handling for different property types
     *
     * @return a string value used as the `dataType` field for model templates, `returnType` for api templates
     */
    @Override
    public String getTypeDeclaration(Property p) {
        if (p instanceof ObjectProperty || p instanceof MapProperty) {
            return "Association";
        }
        else if (p instanceof ArrayProperty) {
            return "List";
        }
        else if (p instanceof IntegerProperty) {
            return "Integer";
        }
        else if (p instanceof AbstractNumericProperty) {
            return "Real";
        }
        else if (p instanceof StringProperty || p instanceof BinaryProperty) {
            return "String";
        }
        else if (p instanceof BooleanProperty) {
            return "Boolean";
        }

        return super.getTypeDeclaration(p);

    }

    /**
     * Optional - swagger type conversion.  This is used to map swagger types in a `Property` into
     * either language specific types via `typeMapping` or into complex models if there is not a mapping.
     *
     * @return a string value of the type or complex model for this property
     * @see io.swagger.models.properties.Property
     */
    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        String type = null;
        if (typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);
            if (languageSpecificPrimitives.contains(type)) {
                return toModelName(type);
            }
        }
        else {
            type = swaggerType;
        }
        return toModelName(type);
    }

    @Override
    public String toDefaultValue(Property p) {
        if (p instanceof ObjectProperty || p instanceof MapProperty) {
            return "<||>";
        }
        else if (p instanceof ArrayProperty) {
            return "{}";
        }
        else if (p instanceof IntegerProperty) {
            IntegerProperty dp = (IntegerProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
            return "Null";
        }
        else if (p instanceof LongProperty) {
            LongProperty dp = (LongProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
            return "Null";
        }
        else if (p instanceof DoubleProperty) {
            DoubleProperty dp = (DoubleProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
            return "Null";
        }
        else if (p instanceof FloatProperty) {
            FloatProperty dp = (FloatProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
            return "Null";
        }
        else if (p instanceof BooleanProperty) {
            BooleanProperty bp = (BooleanProperty) p;
            if (bp.getDefault() != null) {
                return bp.getDefault().toString();
            }
            return "Null";
        }
        else if (p instanceof StringProperty) {
            StringProperty sp = (StringProperty) p;
            if (sp.getDefault() != null) {
                return "\"" + escapeText(sp.getDefault()) + "\"";
            }
            return "Null";
        }
        return "Null";
    }

    @Override
    public String toModelName(String name) {
        String n = super.toModelName(name);
        if (n != null) {
            n = n.replaceAll("[\\W]|_", "");
        }
        return n;
    }

    @Override
    public String toModelFilename(String name) {
        return filterNonAlphanumeric(super.toModelFilename(name));
    }

    @Override
    public String toModelTestFilename(String name) {
        return filterNonAlphanumeric(super.toModelTestFilename(name));
    }

    @Override
    public String toModelDocFilename(String name) {
        return filterNonAlphanumeric(super.toModelDocFilename(name));
    }

    @Override
    public String toModelImport(String name) {
        return filterNonAlphanumeric(super.toModelImport(name));
    }

    public String filterNonAlphanumeric(String name) {
        if (name == null) {
            return null;
        }
        return name.replaceAll("[\\W]|_", "");
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + (invokerPackage != null ? File.separator + invokerPackage : "");
    }

    @Override
    public String modelFileFolder() {
        return outputFolder + (invokerPackage != null ? File.separator + invokerPackage : "");
    }

    @Override
    public String apiTestFileFolder() {
        return outputFolder + (invokerPackage != null ? File.separator + invokerPackage : "");
    }

    @Override
    public String modelTestFileFolder() {
        return outputFolder + (invokerPackage != null ? File.separator + invokerPackage : "");
    }

    @Override
    public String apiDocFileFolder() {
        return outputFolder + (invokerPackage != null ? File.separator + invokerPackage : "");
    }

    @Override
    public String modelDocFileFolder() {
        return outputFolder + (invokerPackage != null ? File.separator + invokerPackage : "");
    }
}