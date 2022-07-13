package nextstep.subway.applicaion.line;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.section.SectionService;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final SectionService sectionService;

    public LineResponse create(LineCreationRequest request) {
        var line = new Line(request.getName(), request.getColor());
        var startSection = sectionService.createInitialSection(line.getId(), request.getSectionCreationRequest());
        line.setStartSection(startSection);
        return LineResponse.fromLine(lineRepository.save(line));
    }

    public LineResponse modify(Long lineId, LineModificationRequest request) {
        var line = getLine(lineId);
        line.changeNameAndColor(request.getName(), request.getColor());

        return LineResponse.fromLine(line);
    }

    public void remove(Long lineId) {
        lineRepository.deleteById(lineId);
        sectionService.removeSections(lineId);
    }

    private Line getLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 노선 ID 입니다."));
    }
}
