import java.util.function.Function;
import java.util.function.Predicate;
import com.bavelsoft.ddd.Initializer;
import com.bavelsoft.ddd.InitializerImpl;
import com.bavelsoft.ddd.Uninitialized;
import javax.inject.Singleton;
import javax.inject.Inject;
import java.util.Collection;

@Singleton
public class CargoFactory {
	private InitializerImpl<Cargo, CargoImpl, ValidationResult> init = new InitializerImpl<>();

	@Inject public void setFooValidation(FooValidater v) {
		init.addValidation(c->new ValidationResult(v), v);
	}

	@Inject public void setBarDefault(BarDefaulter d) {
		init.addDefault(CargoImpl::setDelivery, d);
	}

	public UninitializedCargo create() {
		return new CargoImpl(init);
	}
}

class CargoImpl implements Cargo, UninitializedCargo {
	private Initializer<Cargo, CargoImpl, ValidationResult> initializer;
	private Delivery delivery;

	public CargoImpl(Initializer<Cargo, CargoImpl, ValidationResult> initializer) {
		this.initializer = initializer;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public Cargo validate(Collection<ValidationResult> validationResults) {
		return initializer.initialize(this, validationResults);
	}
}

interface UninitializedCargo extends Uninitialized<Cargo,ValidationResult> {
	public void setDelivery(Delivery delivery);
	public Cargo validate(Collection<ValidationResult> validationResults);
}

interface Cargo {
	public Delivery getDelivery();
}

class Delivery {
}

class ValidationResult {
	String validation;
	ValidationResult(Object o) {
		validation = o.getClass().getSimpleName();
	}
}

interface FooValidater extends Predicate<Cargo> {
}

interface BarDefaulter extends Function<Cargo, Delivery> {
}
