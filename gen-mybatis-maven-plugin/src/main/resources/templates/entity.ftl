package ${packageName};

<#if imports?has_content>
<#list imports as import>
import ${import};
</#list>
</#if>
<#if generateJPA>
import javax.persistence.*;
</#if>
<#if generateLombok>
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
</#if>
<#if generateSwagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if generateOptional>
import java.util.Optional;
</#if>
import com.github.benshi.mybatis.AutoGenMapper;
import com.github.benshi.mybatis.AutoGenColumn;
<#if generateJackson>
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
</#if>

<#if tableComment?has_content>
/**
 * ${className} for ${tableComment}
 *
 */
</#if>
<#if generateLombok>
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
</#if>
<#if generateJPA>
@Entity
@Table(name = "${tableName}")
</#if>
<#if generateSwagger && tableComment?has_content>
@ApiModel(description = "${tableComment}")
</#if>
<#if generateJackson>
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
</#if>
@AutoGenMapper(table = "${tableName}")
public class ${className} {

<#list columns as column>
    <#if column.columnComment?has_content>
    /**
<#list column.columnComment?split("\n") as line>
     * ${line?trim}
</#list>
     */
    </#if>
    <#if generateJPA>
    <#if column.isPrimaryKey>
    @Id
    <#if column.isAutoIncrement>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    </#if>
    @Column(name = "${column.columnName}"<#if !column.isNullable>, nullable = false</#if>)
    </#if>
    <#if generateSwagger && column.columnComment?has_content>
    @ApiModelProperty(value = "${column.columnComment}")
    </#if>
    @AutoGenColumn(name = "${column.columnName}", type = "${column.dataType}"<#if !column.isNullable>, nullable = false</#if><#if column.isPrimaryKey>, pk = true</#if>)
    private ${column.javaTypeSimple} ${column.fieldName};

</#list>
<#if !generateLombok>
    // Default constructor
    public ${className}() {
    }
    
    // All-args constructor
    public ${className}(
    <#list columns as column>
        ${column.javaTypeSimple} ${column.fieldName}<#if column_has_next>,</#if>
    </#list>
    ) {
    <#list columns as column>
        this.${column.fieldName} = ${column.fieldName};
    </#list>
    }

    // Getters and Setters
<#list columns as column>
    public ${column.javaTypeSimple} get${column.fieldName?cap_first}() {
        return ${column.fieldName};
    }

    public void set${column.fieldName?cap_first}(${column.javaTypeSimple} ${column.fieldName}) {
        this.${column.fieldName} = ${column.fieldName};
    }

</#list>
</#if>
<#if generateOptional>
<#list columns as column>
    public Optional<${column.javaTypeSimple}> get${column.fieldName?cap_first}Optional() {
        return Optional.ofNullable(${column.fieldName});
    }

    public void set${column.fieldName?cap_first}Optional(Optional<${column.javaTypeSimple}> ${column.fieldName}Optional) {
        if (${column.fieldName}Optional == null) {
            this.${column.fieldName} = null;
            return;
        }

        this.${column.fieldName} = ${column.fieldName}Optional.orElse(null);
    }
    
</#list>
</#if>
}