package com.bonrix.common.InheritanceExample.SingleTable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.DiscriminatorFormula;


/// From http://www.baeldung.com/hibernate-inheritance
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="product_type", 
discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorFormula("case when author is not null then 1 else 2 end")
public class MyProduct {

		@Id
	    private long productId;
		private String name;
		
	    public long getProductId() {
			return productId;
		}
		public void setProductId(long productId) {
			this.productId = productId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
}
