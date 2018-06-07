 
create table Albums (
   id BIGINT NOT NULL AUTO_INCREMENT,
   filename VARCHAR(300),
   item  VARCHAR(300),
   itemtext    VARCHAR(300),
   relevance long,
   count  long,
   PRIMARY KEY (id)
);


	@Column(name = "filename")
	private String filename;
	@Column(name = "item") //Concept, entity, keyword, category
	private String item;	
	@Column(name = "itemtext")
	private String itemtext;
	@Column(name = "relevance") // relevance. score
    public long relevance;
	@Column(name = "count")
    public long count;
    