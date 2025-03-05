insert into public.curation_warning_detail (id, label, value, curation_warning_id)
values  (54, 'test', 'test', 2),
        (55, 'test', 'test', 52),
        (58, 'test', 'test', 55),
        (59, 'test', 'test', 56),
        (60, 'test', 'test', 54),
        (61, 'test', 'test', 53),
        (62, 'test2', 'test2', 53),
        (102, 'test', 'test', 102),
        (103, 'test', 'test', 103);
        (104, 'test', 'test', 104);

insert into public.curation_warning (target_type, id, created, global_id, last_notification, modified, open, type, institution_id, namespace_id, resource_id)
values  ('resource', 2, '2025-03-05 09:38:32.642000', 'test', '2025-03-05 10:36:16.884000', '2025-03-05 10:36:16.888000', true, 'something', null, null, 49),
        ('resource', 52, '2025-03-05 10:37:10.688000', 'test12169', '2025-03-05 10:37:10.686000', '2025-03-05 10:37:10.693000', true, 'something', null, null, 49),
        ('institution', 55, '2025-03-05 10:38:06.130000', 'test4189', '2025-03-05 10:38:06.130000', '2025-03-05 10:38:06.131000', true, 'something', 2, null, null),
        ('institution', 56, '2025-03-05 10:38:06.729000', 'test21849', '2025-03-05 10:38:06.728000', '2025-03-05 10:38:06.730000', true, 'something', 2, null, null),
        ('institution', 54, '2025-03-05 10:38:05.306000', 'test1282', '2025-03-05 10:41:20.653000', '2025-03-05 10:41:20.657000', true, 'something', 2, null, null),
        ('resource', 53, '2025-03-05 10:37:11.723000', 'test8223', '2025-03-05 10:43:27.193000', '2025-03-05 10:43:27.196000', true, 'something', null, null, 49),
        ('institution', 102, '2025-03-05 14:32:26.188000', 'test18923', '2025-03-05 14:32:26.186000', '2025-03-05 14:32:26.196000', true, 'something2', 2, null, null),
        ('namespace', 103, '2025-03-05 14:32:40.969000', 'test32393', '2025-03-05 14:32:40.967000', '2025-03-05 14:32:40.973000', true, 'something2', null, 1, null);
        ('namespace', 104, '2025-03-05 14:32:40.969000', 'test32394', '2025-03-05 14:32:40.967000', '2025-03-05 14:32:40.973000', true, 'something2', null, 1, null);

insert into public.curation_warning_event (id, actor, comment, created, type, curation_warning_id)
values  (2, 'ANONYMOUS', null, '2025-03-05 09:38:32.643000', 'CREATED', 2),
        (52, 'ANONYMOUS', null, '2025-03-05 10:37:10.690000', 'CREATED', 52),
        (53, 'ANONYMOUS', null, '2025-03-05 10:37:11.724000', 'CREATED', 53),
        (54, 'ANONYMOUS', null, '2025-03-05 10:38:05.309000', 'CREATED', 54),
        (55, 'ANONYMOUS', null, '2025-03-05 10:38:06.130000', 'CREATED', 55),
        (56, 'ANONYMOUS', null, '2025-03-05 10:38:06.729000', 'CREATED', 56),
        (57, 'ANONYMOUS', null, '2025-03-05 10:38:06.729000', 'SNOOZED', 56),
        (102, 'ANONYMOUS', null, '2025-03-05 14:32:26.194000', 'CREATED', 102),
        (103, 'ANONYMOUS', null, '2025-03-05 14:32:40.972000', 'CREATED', 103);
        (103, 'ANONYMOUS', null, '2025-03-05 14:32:40.972000', 'SOLVED', 104);
        (104, 'ANONYMOUS', null, '2025-03-05 14:32:40.972000', 'REOPENED', 55);