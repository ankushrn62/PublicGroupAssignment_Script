Datetime startTime = Datetime.now();
List<String> profileList= new List<String>{'ABM','CPC','FOS','SRH','Underwriter'};
List<User> userList =[select id, name,Profile.Name from User where Profile.Name in :profileList];
List<GroupMember> oldGrpMemberRecords = [select id, UserOrGroupId, Group.DeveloperName,GroupId from GroupMember where UserOrGroupId =: userList and Group.DeveloperName like '%_Users'];
List<GroupMember> newGrpMembers = new List<GroupMember> ();

Map<String,Group> idGroupMap = new Map<String,Group>();
List<Group> publicGroupList = [select id,DeveloperName from Group where Name like '%_Users%' and type='regular'];
for(Group grp : publicGroupList){
	idGroupMap.put(grp.DeveloperName, grp);
}

for(User use : userList){
	String grpID    = idGroupMap.get(use.Profile.Name+'_Users').Id;
	GroupMember gm  = new GroupMember();
	gm.GroupId      = grpID;
	gm.UserOrGroupId = use.id;	
	newGrpMembers.add(gm);
}
System.debug('oldGrpMemberRecords:: '+oldGrpMemberRecords.size());
System.debug('newGrpMembers:: '+newGrpMembers.size());
delete oldGrpMemberRecords;
insert newGrpMembers;
Datetime endTime = Datetime.now();
System.debug('final time: ' + (endTime.getTime() - startTime.getTime());


select id, UserOrGroupId, Group.DeveloperName,GroupId,SystemModstamp from GroupMember where Group.DeveloperName = 'FOS_Users'
select id, name,Profile.Name from User where Profile.Name ='FOS'