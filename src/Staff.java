import java.util.*;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    public List<Request> requests = new ArrayList<Request>();
    public List<String> deletedProductionsActors = new ArrayList<String>();
    Comparator<Object> comparator = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Movie && o2 instanceof Movie) {
                return ((Movie) o1).compareTo((Movie) o2);
            }
            if (o1 instanceof Series && o2 instanceof Series) {
                return ((Series) o1).compareTo((Series) o2);
            }
            if (o1 instanceof Actor && o2 instanceof Actor) {
                return ((Actor) o1).compareTo((Actor) o2);
            }
            if (o1 instanceof Movie && o2 instanceof Series) {
                return ((Movie) o1).productionName.compareTo(((Series) o2).productionName);
            }
            if (o1 instanceof Series && o2 instanceof Movie) {
                return ((Series) o1).productionName.compareTo(((Movie) o2).productionName);
            }
            if (o1 instanceof Movie && o2 instanceof Actor) {
                return ((Movie) o1).productionName.compareTo(((Actor) o2).name);
            }
            if (o1 instanceof Actor && o2 instanceof Movie) {
                return ((Actor) o1).name.compareTo(((Movie) o2).productionName);
            }
            if (o1 instanceof Series && o2 instanceof Actor) {
                return ((Series) o1).productionName.compareTo(((Actor) o2).name);
            }
            if (o1 instanceof Actor && o2 instanceof Series) {
                return ((Actor) o1).name.compareTo(((Series) o2).productionName);
            }
            return 0;
        }
    };
    public SortedSet<T> addedInSystem = new TreeSet<T>(comparator);
    public Staff(Information information, String username, int experience) {
        super(information, username, experience);
    }
    public void addProductionSystem(Production p) {
        addedInSystem.add((T) p);
    }
    public void addActorSystem(Actor a) {
        addedInSystem.add((T) a);
    }
    public void removeProductionSystem(String name) {
        for (T t : addedInSystem) {
            if (t instanceof Production) {
                Production p = (Production) t;
                if (p.productionName.equals(name)) {
                    addedInSystem.remove(t);
                    break;
                }
            }
        }
    }
    public void removeActorSystem(String name) {
        for (T t : addedInSystem) {
            if (t instanceof Actor) {
                Actor a = (Actor) t;
                if (a.name.equals(name)) {
                    addedInSystem.remove(t);
                    break;
                }
            }
        }
    }
    public void updateProduction(Production p) {
        for (T t : addedInSystem) {
            if (!(t instanceof Production)) {
                continue;
            }
            Production p2 = (Production) t;
            if (p2.productionName.equals(p.productionName)) {
                addedInSystem.remove(t);
                addedInSystem.add((T) p);
                break;
            }
        }
    }
    public void updateActor(Actor a) {
        for (T t : addedInSystem) {
            if (!(t instanceof Actor)) {
                continue;
            }
            Actor a2 = (Actor) t;
            if (a2.name.equals(a.name)) {
                addedInSystem.remove(t);
                addedInSystem.add((T) a);
                break;
            }
        }
    }
    public void resolveRequest(Request r) {
        requests.remove(r);
    }
}