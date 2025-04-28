package com.nhnacademy.service.impl;

import com.nhnacademy.exception.projectTag.ProjectTagAlreadyExistsException;
import com.nhnacademy.exception.projectTag.ProjectTagNotFoundException;
import com.nhnacademy.exception.tag.TagNotFoundException;
import com.nhnacademy.exception.task.TaskNotFoundException;
import com.nhnacademy.model.projectTag.dto.ProjectTagRegisterRequest;
import com.nhnacademy.model.projectTag.dto.RegisterProjectTagRequest;
import com.nhnacademy.model.projectTag.entity.ProjectTag;
import com.nhnacademy.model.tag.dto.ResponseGetTagDto;
import com.nhnacademy.model.tag.dto.ResponseGetTagsDto;
import com.nhnacademy.model.tag.entity.Tag;
import com.nhnacademy.model.task.entity.Task;
import com.nhnacademy.repository.ProjectTagRepository;
import com.nhnacademy.repository.TagRepository;
import com.nhnacademy.repository.TaskRepository;
import com.nhnacademy.service.ProjectTagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectTagServiceImpl implements ProjectTagService {

    private final ProjectTagRepository projectTagRepository;
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;

    @Override
    public ProjectTag findByTaskId(Long projectTagId) {
        return projectTagRepository.findById(projectTagId)
                .orElseThrow(() -> new ProjectTagNotFoundException(projectTagId));
    }

    @Override
    public ResponseGetTagsDto findTagNameByTaskId(Long taskId) {
        if(!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        List<ProjectTag> projectTagList = projectTagRepository.findAllByTask_TaskId(taskId);
        ResponseGetTagsDto responseGetTagsDto = new ResponseGetTagsDto();
        for(ProjectTag projectTag : projectTagList) {
            responseGetTagsDto.getTagList().add(
                    new ResponseGetTagDto(
                            projectTag.getTag().getTagId(),
                            projectTag.getTag().getTagName())
            );
        }
        return responseGetTagsDto;
    }

    @Override
    public ProjectTag registerProjectTag(ProjectTagRegisterRequest request) {
        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new TagNotFoundException(request.getTagId()));
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(request.getTaskId()));
        ProjectTag projectTag = new ProjectTag(tag,task);
        if(projectTagRepository.existsByTag_TagIdAndTask_TaskId(request.getTagId(), request.getTaskId())) {
            throw new ProjectTagAlreadyExistsException();
        }
        return projectTagRepository.save(projectTag);
    }

    @Override
    public void registerProjectTagList(RegisterProjectTagRequest request, long taskId) {
        List<Long> tagIds = request.getTagIds();
        for(Long tagId : tagIds) {
            registerProjectTag(new ProjectTagRegisterRequest(tagId,taskId));
        }
    }

    @Override
    public void delete(long projectTagId) {
        if(!projectTagRepository.existsById(projectTagId)) {
            throw new ProjectTagNotFoundException(projectTagId);
        }
        projectTagRepository.deleteById(projectTagId);
    }

    @Override
    public void deleteByTaskId(long taskId) {
        if(!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        projectTagRepository.deleteByTask_TaskId(taskId);
    }
}
