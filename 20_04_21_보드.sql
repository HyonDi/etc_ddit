insert into board(bno, title, writer, content)
select board_seq.nextval, title, writer, content
from board;
commit;

SELECT board_seq.nextval
FROM dual;

SELECT board_seq.currval
FROM dual;

insert into reply(rno, bno, replytext,replyer,regdate,updatedate )
select reply_seq.nextval,bno,replytext, replyer, regdate, updatedate
from reply;
commit;

select *
from board
where bno=243;

-- ���̵�����
insert into pds(pno, writer, content, title)
select pds_seq.nextval,writer,content,title
from pds;
commit;

-- listȭ�鿡�� ÷�����Ͽ���, �����̸������Ϸ��� join�ؾ���.
-- 