package cn.org.formservice.demo.model;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Entry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TagPagedQuery extends StudentQuery {
    @Entry(type = EntryType.PagedTag)
    private int nextId;

    @Entry(type = EntryType.PageSize)
    private int pageSize = 10;
}