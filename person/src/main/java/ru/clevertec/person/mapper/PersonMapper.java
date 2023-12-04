package ru.clevertec.person.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.person.dto.PersonRequest;
import ru.clevertec.person.dto.PersonResponse;
import ru.clevertec.person.model.Person;
import ru.clevertec.starter.model.Session;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {
    @Mapping(target = "session",source = "session")
    @Mapping(target = "id",source = "person.id")
    @Mapping(target = "login",source = "person.login")
    @Mapping(target = "name",source = "person.name")
    PersonResponse toResponse(Person person, Session session);
    Person fromRequest(PersonRequest request);
}
