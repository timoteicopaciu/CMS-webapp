package cms.web.converter;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseConverter<Model, Dto>
        implements Converter<Model, Dto> {

    public List<Dto> convertModelsToDtos(Collection<Model> models) {
        return models.stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }

    public List<Model> convertDtosToModel(Collection<Dto> models) {
        return models.stream()
                .map(this::convertDtoToModel)
                .collect(Collectors.toList());
    }
}
