This check ensures that:

- It can check assignment of literals (e.g. `@FooBar(lorem = "ipsum")`, `@FooBarArray(value = { "Foo", "Bar" })` )
- All `@Transactional` annotation usages are on method level and only on public methods
- Any method, belonging to a bean class and has an entity in its parameters or return type&nbsp;has `@Transactional(propagation = Propagation.MANDATORY)`
- An Aspect bean does not contain any `@Transactional` annotation



## Properties

| name | description | type | default values | since |
|------|-------------|------|----------------|-------|
| patterns | classes excluded | [String[]][stringArray] | `[]` | 1.0 |

[stringArray]: https://checkstyle.sourceforge.io/property_types.html#String.5B.5D
